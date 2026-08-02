package com.umc.halo.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Calendar
import com.umc.halo.domain.model.calendar.CalendarDay
import com.umc.halo.domain.model.calendar.CompletedBook
import com.umc.halo.domain.model.calendar.DayMark
import com.umc.halo.domain.model.calendar.MonthSummary
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

/**
 * 캘린더 화면 진입점
 * 화면 이동은 [onNavigateToStorybookList]·[onNavigateToThemeBox]·[onNavigateToThemeBoxStorybook]
 * [onNavigateToChapterResult] 콜백으로 위임
 *
 */
@Composable
fun CalendarScreen(
    vm: CalendarViewModel = hiltViewModel(),
    onNavigateToStorybookList: () -> Unit = {},
    onNavigateToThemeBox: () -> Unit = {},
    onNavigateToThemeBoxStorybook: (Long) -> Unit = {},
    onNavigateToChapterResult: (Long, Long) -> Unit = { _, _ -> }
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    // 조회 실패 안내
    // TODO: 표시 방식은 디자인 확정 후 교체 (지금은 토스트)
    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        vm.onEvent(CalendarUiEvent.ErrorShown)
    }

    CalendarContent(
        state = state,
        onEvent = { event ->
            when (event) {
                // 화면 이동 이벤트는 화면 경계에서 처리
                // 모달에서 눌린 경우가 있어 먼저 모달을 닫음
                CalendarUiEvent.OnStartRecordingClicked -> {           // 빈 모달 '시작하기' + 하단 '바로 시작하기'
                    vm.onEvent(CalendarUiEvent.OnDismissModal)
                    onNavigateToStorybookList()
                }
                // 모달 완성 스토리북 카드 → 테마함의 해당 스토리북
                is CalendarUiEvent.OnCompletedStorybookClicked -> {
                    vm.onEvent(CalendarUiEvent.OnDismissModal)
                    onNavigateToThemeBoxStorybook(event.storybookId)
                }
                // 모달 '장 기록중' 카드 → 그 장의 완료 결과 화면
                // 서버가 장 고유 id 를 안 줘서 장 순서를 chapterId 자리에 넘긴다 (CalendarUiEvent 주석 참고)
                is CalendarUiEvent.OnChapterClicked -> {
                    vm.onEvent(CalendarUiEvent.OnDismissModal)
                    onNavigateToChapterResult(event.storybookId, event.chapterOrder.toLong())
                }
                CalendarUiEvent.OnSummaryDetailClicked -> onNavigateToThemeBox()  // 하단 '자세히 보러가기'

                // 그 외(달 이동·날짜 클릭·모달 닫기)는 VM 이 상태로 처리
                else -> vm.onEvent(event)
            }
        }
    )
}

@Composable
private fun CalendarContent(
    state: CalendarUiState,
    onEvent: (CalendarUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            CalendarTopSummaryCard(
                month = state.month,
                count = state.recordedChapterCount
            )

            Spacer(Modifier.height(36.dp))

            CalendarMonthView(
                year = state.year,
                month = state.month,
                days = state.days,
                canGoNext = state.canGoToNextMonth,
                onEvent = onEvent
            )

            Spacer(Modifier.height(30.dp))

            CalendarMonthSummary(
                month = state.month,
                summary = state.summary,
                onEvent = onEvent
            )

            Spacer(Modifier.height(30.dp))
        }

        // 날짜 클릭 시 기록 모달(오버레이)
        // 서버 응답을 기다리는 동안에도 모달은 떠 있고 안쪽만 로딩으로 표시
        state.selectedDay?.let { day ->
            CalendarDayRecordModal(
                month = state.month,
                day = day,
                record = state.selectedRecord,
                isLoading = state.isDayRecordLoading,
                onEvent = onEvent
            )
        }
    }
}

/** 상단 요약 카드: "N월 한 달 동안 X개의 장을 기록했어요." */
@Composable
private fun CalendarTopSummaryCard(
    month: Int,
    count: Int
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .background(White)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "${month}월 한 달 동안",
                style = HaloType.body02SemiBold,
                color = Gray500
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "$count",
                    style = HaloType.title02Bold,
                    color = Primary500
                )
                Text(
                    text = "개의 장을 기록했어요.",
                    style = HaloType.body02Medium,
                    color = Gray500,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

// ---- Preview ----

// 화면1: 진행중 O & 완성 O (현재 월 → 오른쪽 화살표 없음)
@Preview(showBackground = true, showSystemUi = true, name = "캘린더 - 진행중·완성 존재")
@Composable
private fun CalendarScreenFullPreview() {
    HaloTheme {
        CalendarContent(
            state = CalendarUiState(
                recordedChapterCount = 3,
                canGoToNextMonth = false,
                days = previewDays(withRecords = true),
                summary = MonthSummary(
                    completedCount = 3,
                    inProgressCount = 1,
                    completedBooks = listOf(
                        CompletedBook(1),
                        CompletedBook(2),
                        CompletedBook(3)
                    )
                )
            ),
            onEvent = {}
        )
    }
}

// 화면2: 진행중 X & 완성 X (과거 월 가정 → 좌·우 화살표 모두 표시)
@Preview(showBackground = true, showSystemUi = true, name = "캘린더 - 진행중·완성 없음")
@Composable
private fun CalendarScreenEmptyPreview() {
    HaloTheme {
        CalendarContent(
            state = CalendarUiState(
                recordedChapterCount = 0,
                canGoToNextMonth = true,
                days = previewDays(withRecords = false),
                summary = MonthSummary(0, 0, emptyList())
            ),
            onEvent = {}
        )
    }
}

private fun previewDays(withRecords: Boolean): List<CalendarDay> {
    val year = 2026
    val month = 5
    val cal = Calendar.getInstance().apply { clear(); set(year, month - 1, 1) }
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val leadingBlanks = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    // 스토리북 완료(진주황) vs 장 완료(연주황) 마크를 섞어서 확인
    val storybookCompletedDays = if (withRecords) setOf(10, 19) else emptySet()
    val pageCompletedDays = if (withRecords) setOf(9, 11) else emptySet()

    val cells = mutableListOf<CalendarDay>()
    repeat(leadingBlanks) { cells.add(CalendarDay(day = 0, inCurrentMonth = false)) }
    for (d in 1..daysInMonth) {
        val mark = when (d) {
            in storybookCompletedDays -> DayMark.STORYBOOK_COMPLETED
            in pageCompletedDays -> DayMark.PAGE_COMPLETED
            else -> null
        }
        cells.add(
            CalendarDay(
                day = d,
                inCurrentMonth = true,
                mark = mark,
                isToday = d == 25
            )
        )
    }
    while (cells.size % 7 != 0) cells.add(CalendarDay(day = 0, inCurrentMonth = false))
    return cells
}
