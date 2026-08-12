package com.umc.halo.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.calendar.CalendarDay
import com.umc.halo.domain.model.calendar.DayMark
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 디자인 값(수정 용이하게 상수로 모음)
private val CellHeight = 44.dp          // 요일/날짜 한 칸 높이
private val TodayCircleSize = 30.dp      // 오늘날짜 - 주황 원
private val DayMarkWidth = 30.dp         // 날짜칸 서비스 로고 마크
private val DayMarkHeight = 26.dp
private val DividerColor = Gray200       // 달력 격자(구분선) 색상
private const val DisabledArrowAlpha = 0.3f  // 더 넘어갈 달이 없을 때 화살표 투명도

private val Weekdays = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

/**
 * 달력 영역: "YYYY년 M월" 헤더 + 요일 헤더 + 주 단위 그리드
 * 모든 날짜 칸은 클릭 시 그 날 기록 모달을 보여줌
 */
@Composable
fun CalendarMonthView(
    year: Int,
    month: Int,
    days: List<CalendarDay>,
    canGoNext: Boolean,          // 현재 월보다 과거일 때만 true → 오른쪽(다음 달) 화살표 표시
    onEvent: (CalendarUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 헤더: "YYYY년 M월" 과 < > - 좌우 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${year}년 ${month}월",
                style = HaloType.body01Medium,
                color = Gray500,
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar_left_arrow),
                    contentDescription = "이전 달",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onEvent(CalendarUiEvent.OnPrevMonthClicked) }
                )
                // 현재 월에선 다음 달로 넘어갈 수 없게 함
                // 자리는 그대로 두고 흐리게 + 클릭 비활성으로만 표시
                Icon(
                    painter = painterResource(R.drawable.ic_calendar_right_arrow),
                    contentDescription = if (canGoNext) "다음 달" else null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (canGoNext) 1f else DisabledArrowAlpha)
                        .clickable(enabled = canGoNext) {
                            onEvent(CalendarUiEvent.OnNextMonthClicked)
                        }
                )
            }
        }

        // 요일 헤더
        WeekdayHeader()

        // 주 단위 그리드 (7일씩)
        days.chunked(7).forEach { week ->
            WeekRow(
                week = week,
                onDayClick = { day -> onEvent(CalendarUiEvent.OnDayClicked(day)) }
            )
        }
    }
}

@Composable
private fun WeekdayHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(CellHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Weekdays.forEachIndexed { index, label ->
            if (index > 0) VerticalHairline(height = 12.dp)
            Text(
                text = label,
                style = HaloType.caption01Regular,
                color = Gray500,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WeekRow(
    week: List<CalendarDay>,
    onDayClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 주 상단 구분선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerColor)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CellHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            week.forEachIndexed { index, day ->
                if (index > 0) VerticalHairline(height = 28.dp)
                DayCell(
                    day = day,
                    modifier = Modifier.weight(1f),
                    onClick = { onDayClick(day.day) }
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    day: CalendarDay,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // 요일 정렬용 빈칸
    if (!day.inCurrentMonth) {
        Box(modifier = modifier.height(CellHeight))
        return
    }
    // 미래 날짜는 눌러도 모달을 띄우지 않음
    Box(
        modifier = modifier
            .height(CellHeight)
            .clickable(enabled = !day.isFuture) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 오늘날짜 - 주황 원
        if (day.isToday) {
            Box(
                modifier = Modifier
                    .size(TodayCircleSize)
                    .clip(CircleShape)
                    .background(Primary500)
            )
        }
        // 기록한 날 서비스 로고 마크 — 장 완료 vs 스토리북 완료에 따라 다른 아이콘
        day.mark?.let { mark ->
            val markRes = when (mark) {
                DayMark.PAGE_COMPLETED -> R.drawable.ic_calendar_storybook_page_completed_mark
                DayMark.STORYBOOK_COMPLETED -> R.drawable.ic_calendar_storybook_completed_mark
            }
            Icon(
                painter = painterResource(markRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(width = DayMarkWidth, height = DayMarkHeight)
            )
        }
        // 날짜 숫자
        Text(
            text = "${day.day}",
            style = HaloType.body02Regular,
            color = if (day.isToday) White else Gray500,
            textAlign = TextAlign.Center
        )
    }
}

/** 날짜 칸 사이 세로 구분선 */
@Composable
private fun VerticalHairline(height: Dp) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(DividerColor)
    )
}
