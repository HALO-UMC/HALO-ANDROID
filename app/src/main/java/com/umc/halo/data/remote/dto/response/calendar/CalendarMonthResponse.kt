package com.umc.halo.data.remote.dto.response.calendar

/**
 * GET /api/v1/calendar?year={year}&month={month} 응답 (캘린더 메인 조회)
 *
 */
data class CalendarMonthResponse(
    val stats: CalendarStatsResponse?,
    val recordedDays: List<RecordedDayResponse>?,
    val completedStorybooks: List<CompletedStorybookResponse>?
)

/** 상단 요약 통계 */
data class CalendarStatsResponse(
    val completedPageCount: Int,        // 그 달에 완료한 장 수 → "N개의 장을 기록했어요"
    val completedStorybookCount: Int,   // 그 달에 완성한 스토리북 수
    val inProgressStorybookCount: Int   // 진행 중인 스토리북 수
)

/**
 * 기록이 있는 날 관련
 *
 * @param hasCompletedStorybook true = 그 날 스토리북을 완성함(진한 마크) / false = 장만 완료(연한 마크)
 */
data class RecordedDayResponse(
    val day: Int,
    val hasCompletedStorybook: Boolean
)

/**
 * 그 달에 완성한 스토리북
 *
 * 책등 그림은 앱 자산이라 서버는 storybookId 만 줌
 */
data class CompletedStorybookResponse(
    val storybookId: Long
)
