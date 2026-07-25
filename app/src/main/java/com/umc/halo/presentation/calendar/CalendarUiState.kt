package com.umc.halo.presentation.calendar

import com.umc.halo.domain.model.calendar.CalendarDay
import com.umc.halo.domain.model.calendar.DayRecord
import com.umc.halo.domain.model.calendar.MonthSummary

/**
 * 캘린더 화면 상태
 * 조건별 4개 화면을 하나의 상태로 표현
 *  - 상단 요약: [recordedChapterCount] (0 이면 "0개의 장을 기록했어요")
 *  - 하단 요약: [summary].isEmpty 로 [완성 + 진행중 + 책꽂이] vs [완성 + '바로 시작하기' CTA] 분기
 *  - 모달   : [selectedRecord] 가 null 이 아니면 표시하고, hasRecord 로 기록 모달 vs 빈 모달 분기
 *
 * 달력은 과거~현재 월만 조회 가능 → [canGoToNextMonth] 가 false 면 오른쪽(다음 달) 화살표를 숨김
 */
data class CalendarUiState(
    val year: Int = 2026,
    val month: Int = 5,
    val recordedChapterCount: Int = 0,          // 상단 "N개의 장을 기록했어요"
    val days: List<CalendarDay> = emptyList(),  // 달력 칸
    val summary: MonthSummary = MonthSummary(0, 0, emptyList()),
    val selectedRecord: DayRecord? = null,      // 모달 (null = 닫힘)
    val canGoToNextMonth: Boolean = false       // 현재 월보다 과거면 true → 오른쪽 화살표 표시
)
