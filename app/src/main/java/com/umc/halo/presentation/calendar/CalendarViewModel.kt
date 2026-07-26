package com.umc.halo.presentation.calendar

import com.umc.halo.domain.model.calendar.CalendarDay
import com.umc.halo.domain.model.calendar.CompletedBook
import com.umc.halo.domain.model.calendar.DayMark
import com.umc.halo.domain.model.calendar.DateCompletedStorybook
import com.umc.halo.domain.model.calendar.DateCompletedChapter
import com.umc.halo.domain.model.calendar.DayRecord
import com.umc.halo.domain.model.calendar.MonthSummary
import com.umc.halo.presentation.base.BaseViewModel
import java.util.Calendar

/**
 * 캘린더 화면 ViewModel
 * 서버 연동 전까지 더미 데이터로 동작하며 네트워크 연동 시 Repository 를 주입하면서
 * @HiltViewModel 로 승격 예정
 *
 * 조회 범위: **과거 ~ 현재 월(오늘 기준)** 까지만
 * 현재 월에선 다음 달로 넘어가는 버튼을 막히게 오른쪽 화살표를 숨김
 *
 * 달 이동은 아직 서버 연동 전이라 "라벨/그리드만 바뀌는 임시 stub"
 * 기록·오늘·요약 더미는 현재 월(anchor)에만 반영
 */
class CalendarViewModel : BaseViewModel<CalendarUiState, CalendarUiEvent>(CalendarUiState()) {

    // 실제 오늘 — 현재 월을 조회 상한선으로 사용
    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH) + 1   // Calendar.MONTH 는 0-based
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    // [임시 확인용] 캘린더 빈 상태(화면2: 진행중X·완성X) 미리보기 토글
    // true  = 화면2(빈 상태 · 바로 시작하기 CTA), false = 화면1(진행중 O·완성 O)
    private val previewEmptyState = false

    // 현재 월에만 존재하는 더미 기록(날짜 → 모달 내용)
    // 빈 상태 미리보기(previewEmptyState)면 기록을 아예 비워 달력 마크·기록 모달도 안 뜨게 함
    //
    // chapterId 는 장 번호와 다른 값이라는 걸 드러내려고 (스토리북id * 100 + 장번호) 로 임의 부여
    // 서버 연동 시 실제 장 id 로 대체
    private val dummyRecords: Map<Int, DayRecord> = if (previewEmptyState) emptyMap() else mapOf(
        9 to DayRecord(
            month = currentMonth, day = 9,
            completedChapters = listOf(
                DateCompletedChapter(storybookId = 101, chapterId = 10106, title = "오래전 당신", chapter = 6)
            )
        ),
        10 to DayRecord(
            month = currentMonth, day = 10,
            completedStorybook = DateCompletedStorybook(201, "오래전 당신"),
            completedChapters = listOf(
                DateCompletedChapter(storybookId = 101, chapterId = 10105, title = "오래전 당신", chapter = 5),
                DateCompletedChapter(storybookId = 102, chapterId = 10205, title = "나란히 걷는 날", chapter = 5)
            )
        ),
        11 to DayRecord(
            month = currentMonth, day = 11,
            completedChapters = listOf(
                DateCompletedChapter(storybookId = 103, chapterId = 10303, title = "가족의 온도", chapter = 3)
            )
        ),
        19 to DayRecord(
            month = currentMonth, day = 19,
            completedStorybook = DateCompletedStorybook(202, "당신 사용 설명서"),
            completedChapters = listOf(
                DateCompletedChapter(storybookId = 101, chapterId = 10107, title = "오래전 당신", chapter = 7)
            )
        )
    )

    override fun onEvent(event: CalendarUiEvent) {
        when (event) {
            CalendarUiEvent.OnPrevMonthClicked -> moveMonth(-1)
            CalendarUiEvent.OnNextMonthClicked -> moveMonth(+1)

            is CalendarUiEvent.OnDayClicked -> {
                // 기록이 있으면 기록 모달, 없으면 빈 모달(아직 기록이 없어요)
                val record = recordFor(event.day)
                    ?: DayRecord(month = currentState.month, day = event.day)
                updateState { copy(selectedRecord = record) }
            }

            CalendarUiEvent.OnDismissModal -> updateState { copy(selectedRecord = null) }

            // 아래 4개는 화면 이동 이벤트 → CalendarScreen 이 NavGraph 콜백으로 처리
            CalendarUiEvent.OnStartRecordingClicked,          // → 스토리북(전체탭)
            is CalendarUiEvent.OnCompletedStorybookClicked,   // → 테마함
            is CalendarUiEvent.OnChapterClicked,              // → 장 완료 결과 화면(chapter_result)
            CalendarUiEvent.OnSummaryDetailClicked -> Unit    // → 테마함
        }
    }

    init {
        // 현재 월에서 시작 — previewEmptyState 에 따라 화면1(진행중 O·완성 O) / 화면2(빈 상태)로 채움
        updateState {
            copy(
                year = currentYear,
                month = currentMonth,
                recordedChapterCount = if (previewEmptyState) 0 else 3,
                days = buildDays(currentYear, currentMonth),
                canGoToNextMonth = canGoNext(currentYear, currentMonth), // 현재 월 → false(오른쪽 화살표 숨김)
                summary = if (previewEmptyState) {
                    // 화면2: 완료 책 0개 → summary.isEmpty == true → '바로 시작하기' CTA 상태
                    MonthSummary(completedCount = 0, inProgressCount = 0, completedBooks = emptyList())
                } else {
                    MonthSummary(
                        // 완성 스토리북 개수 = 책장에 꽂히는 완료 책 수(10장 전부 완료한 책만)
                        completedCount = 3,
                        inProgressCount = 1,
                        completedBooks = listOf(
                            CompletedBook(201, "오래전 당신"),
                            CompletedBook(202, "당신의 1호 팬"),
                            CompletedBook(209, "손을 내미는 연습"),
                            CompletedBook(204, "오늘은 내가 먼저")
                        )
                    )
                }
            )
        }
    }

    private fun moveMonth(delta: Int) {
        // 현재 월보다 미래로는 못 넘어감
        if (delta > 0 && !canGoNext(currentState.year, currentState.month)) return

        val total = currentState.year * 12 + (currentState.month - 1) + delta
        val newYear = total / 12
        val newMonth = total % 12 + 1
        updateState {
            copy(
                year = newYear,
                month = newMonth,
                days = buildDays(newYear, newMonth),
                canGoToNextMonth = canGoNext(newYear, newMonth),
                selectedRecord = null
            )
        }
    }

    private fun recordFor(day: Int): DayRecord? =
        if (isAnchorMonth(currentState.year, currentState.month)) dummyRecords[day] else null

    /**
     * 달력 그리드 생성
     * 그 달의 실제 일수(28/29/30/31)와 1일 요일에 맞춰 앞뒤 빈칸을 채워 주 단위로 만듦
     * 기록/오늘 표시는 더미가 있는 현재 월만(오늘 이전 날짜만 기록 표시)
     */
    private fun buildDays(year: Int, month: Int): List<CalendarDay> {
        val anchor = isAnchorMonth(year, month)

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
            // 오늘 이전(포함)의 현재 월 기록만 마크 표시
            val record = if (anchor && d <= currentDay) dummyRecords[d] else null
            cells.add(
                CalendarDay(
                    day = d,
                    inCurrentMonth = true,
                    mark = record?.toDayMark(),
                    isToday = anchor && d == currentDay
                )
            )
        }
        // 마지막 주 채우기용 뒤쪽 빈칸
        while (cells.size % 7 != 0) cells.add(CalendarDay(day = 0, inCurrentMonth = false))
        return cells
    }

    /**
     * 날짜 칸에 찍히는 서비스 로고 종류 판정
     * - completedStorybook 이 있으면 = 그 날 어떤 스토리북의 마지막(10번째) 장을 완료해 그 책이 완성된 날 → STORYBOOK_COMPLETED
     * - 없으면 = 스토리북 완성 없이 장만 완료한 날 → PAGE_COMPLETED
     *  스토리북 완성 여부는 데이터가 결정
     */
    private fun DayRecord.toDayMark(): DayMark =
        if (completedStorybook != null) DayMark.STORYBOOK_COMPLETED else DayMark.PAGE_COMPLETED

    private fun isAnchorMonth(year: Int, month: Int) = year == currentYear && month == currentMonth

    /** 표시 중인 (year, month) 가 현재 월보다 과거인가 → true 면 다음 달 이동/오른쪽 화살표 허용 */
    private fun canGoNext(year: Int, month: Int): Boolean =
        (year * 12 + month) < (currentYear * 12 + currentMonth)
}
