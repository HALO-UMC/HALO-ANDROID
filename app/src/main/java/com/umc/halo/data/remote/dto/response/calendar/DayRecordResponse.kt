package com.umc.halo.data.remote.dto.response.calendar

/**
 * GET /api/v1/calendar/{date} 응답 (일별 기록 조회)
 * 날짜 칸을 눌렀을 때 뜨는 모달의 내용
 *
 */
data class DayRecordResponse(
    val date: String?,
    val storybooks: List<DayCompletedStorybookResponse>?,
    val chapters: List<DayCompletedChapterResponse>?
)

/** 그 날 완성한 스토리북 (모달 '완성 스토리북' 섹션) */
data class DayCompletedStorybookResponse(
    val storybookId: Long,
    val title: String,
    val storybookImageUrl: String?
)

/**
 * 그 날 기록한 장 (모달 '장 기록중' 섹션)
 *
 * @param completedChapterOrder 그 날 완료한 마지막 장 순서 → "N장 기록을 완료했어요!"
 */
data class DayCompletedChapterResponse(
    val storybookId: Long,
    val title: String,
    val completedChapterOrder: Int
)
