package com.umc.halo.presentation.calendar

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.calendar.CalendarDay
import com.umc.halo.domain.model.calendar.MonthSummary
import com.umc.halo.domain.model.calendar.RecordedDay
import com.umc.halo.domain.repository.calendar.CalendarRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * 캘린더 화면 ViewModel
 *
 * 달력(그 달의 실제 일수·1일 요일 정렬)은 여기서 계산하고
 * 그 위에 얹을 데이터(기록된 날·요약·날짜별 기록)는 서버에서 받아옴
 *
 * 조회 범위: **과거 ~ 현재 월(오늘 기준)** 까지만
 * 현재 월에선 다음 달로 못 넘어가게 오른쪽 화살표를 숨김 처리
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
) : BaseViewModel<CalendarUiState, CalendarUiEvent>(CalendarUiState()) {

    // 실제 오늘 — 현재 월을 조회 상한선으로 사용
    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH) + 1   // Calendar.MONTH 는 0-based
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    // 진행 중인 요청 — 달을 빠르게 넘기거나 날짜를 연달아 누를 때
    // 늦게 도착한 이전 응답이 새 화면을 덮어쓰지 않도록 이전 요청을 취소
    private var monthJob: Job? = null
    private var dayRecordJob: Job? = null

    init {
        // 현재 월부터 시작
        loadMonth(currentYear, currentMonth)
    }

    override fun onEvent(event: CalendarUiEvent) {
        when (event) {
            CalendarUiEvent.OnPrevMonthClicked -> moveMonth(-1)
            CalendarUiEvent.OnNextMonthClicked -> moveMonth(+1)

            // 기록이 있으면 기록 모달, 없으면 빈 모달(아직 기록이 없어요)
            is CalendarUiEvent.OnDayClicked -> loadDayRecord(event.day)

            CalendarUiEvent.OnDismissModal -> {
                dayRecordJob?.cancel()
                updateState { copy(selectedDay = null, selectedRecord = null, isDayRecordLoading = false) }
            }

            CalendarUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }

            // 아래 4개는 화면 이동 이벤트 → CalendarScreen 이 NavGraph 콜백으로 처리
            CalendarUiEvent.OnStartRecordingClicked,          // → 스토리북(전체탭)
            is CalendarUiEvent.OnCompletedStorybookClicked,   // → 테마함
            is CalendarUiEvent.OnChapterClicked,              // → 장 완료 결과 화면(chapter_result)
            CalendarUiEvent.OnSummaryDetailClicked -> Unit    // → 테마함
        }
    }

    /**
     * 그 달의 현황을 서버에서 받아옴
     *
     * 응답을 기다리는 동안 화면이 멈춘 것처럼 보이지 않도록
     * 달력 뼈대(마크 없는 빈 달력)를 먼저 그려두고 마크·요약만 나중에 채우기
     */
    private fun loadMonth(year: Int, month: Int) {
        monthJob?.cancel()
        monthJob = viewModelScope.launch {
            updateState {
                copy(
                    year = year,
                    month = month,
                    days = buildDays(year, month, emptyList()),
                    canGoToNextMonth = canGoNext(year, month),
                    // 달이 바뀌면 열려 있던 모달은 닫기
                    selectedDay = null,
                    selectedRecord = null,
                    isLoading = true
                )
            }

            runCatching { calendarRepository.getMonth(year, month) }
                .onSuccess { calendarMonth ->
                    updateState {
                        copy(
                            recordedChapterCount = calendarMonth.completedChapterCount,
                            days = buildDays(year, month, calendarMonth.recordedDays),
                            summary = calendarMonth.summary
                        )
                    }
                }
                .onFailure {
                    // 실패하면 이전 달 수치가 남지 않도록 요약을 비우고 안내
                    updateState {
                        copy(
                            recordedChapterCount = 0,
                            summary = MonthSummary(0, 0, emptyList()),
                            errorMessage = MONTH_LOAD_FAILED_MESSAGE
                        )
                    }
                }

            updateState { copy(isLoading = false) }
        }
    }

    /** 날짜 클릭 → 그 날 기록 조회 (응답 전까지 모달은 로딩 상태로 열려 있음) */
    private fun loadDayRecord(day: Int) {
        // 코루틴 안에서 currentState 를 읽으면 그 사이 달이 바뀔 수 있어 지금 값을 잡아둠
        val year = currentState.year
        val month = currentState.month

        dayRecordJob?.cancel()
        dayRecordJob = viewModelScope.launch {
            updateState {
                copy(selectedDay = day, selectedRecord = null, isDayRecordLoading = true)
            }

            runCatching { calendarRepository.getDayRecord(year, month, day) }
                .onSuccess { record ->
                    updateState { copy(selectedRecord = record, isDayRecordLoading = false) }
                }
                .onFailure {
                    // 빈 모달("아직 기록이 없어요")로 오해하지 않도록 모달을 닫고 실패로 알림
                    updateState {
                        copy(
                            selectedDay = null,
                            isDayRecordLoading = false,
                            errorMessage = DAY_RECORD_LOAD_FAILED_MESSAGE
                        )
                    }
                }
        }
    }

    private fun moveMonth(delta: Int) {
        // 현재 월보다 미래로는 못 넘어감
        if (delta > 0 && !canGoNext(currentState.year, currentState.month)) return

        val total = currentState.year * 12 + (currentState.month - 1) + delta
        loadMonth(year = total / 12, month = total % 12 + 1)
    }

    /**
     * 달력 그리드 생성
     * 그 달의 실제 일수(28/29/30/31)와 1일 요일에 맞춰 앞뒤 빈칸을 채워 주 단위로 만듦
     * 마크는 서버가 준 [recordedDays] 로만 찍는다
     */
    private fun buildDays(year: Int, month: Int, recordedDays: List<RecordedDay>): List<CalendarDay> {
        val marks = recordedDays.associate { it.day to it.toDayMark() }
        val isCurrentMonth = year == currentYear && month == currentMonth

        val cal = Calendar.getInstance().apply {
            clear()
            set(year, month - 1, 1)           // Calendar.MONTH 는 0-based
        }
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val leadingBlanks = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY  // 1일이 무슨 요일인지(SUN=0칸)

        val cells = mutableListOf<CalendarDay>()
        // 1일 요일 정렬용 앞쪽 빈칸
        repeat(leadingBlanks) { cells.add(CalendarDay(day = 0, inCurrentMonth = false)) }
        // 실제 날짜
        for (d in 1..daysInMonth) {
            cells.add(
                CalendarDay(
                    day = d,
                    inCurrentMonth = true,
                    mark = marks[d],
                    isToday = isCurrentMonth && d == currentDay
                )
            )
        }
        // 마지막 주 채우기용 뒤쪽 빈칸
        while (cells.size % 7 != 0) cells.add(CalendarDay(day = 0, inCurrentMonth = false))
        return cells
    }

    /** 표시 중인 (year, month) 가 현재 월보다 과거인가 → true 면 다음 달 이동/오른쪽 화살표 허용 */
    private fun canGoNext(year: Int, month: Int): Boolean =
        (year * 12 + month) < (currentYear * 12 + currentMonth)

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val MONTH_LOAD_FAILED_MESSAGE = "기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
        const val DAY_RECORD_LOAD_FAILED_MESSAGE = "그 날의 기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}
