package com.umc.halo.domain.model.calendar

/**
 * 날짜 클릭 시 모달에 표시할 해당 일의 기록 (GET /api/v1/calendar/{date})
 * completedStorybooks / completedChapters 가 모두 비어있으면 기록이 없는 모달로 표시
 */
data class DayRecord(
    val month: Int,
    val day: Int,
    val completedStorybooks: List<DateCompletedStorybook> = emptyList(),  // 그 날 완성한 스토리북(있으면 먼저 표시)
    val completedChapters: List<DateCompletedChapter> = emptyList()       // 그 날 완료한 장(최신순)
) {
    val hasRecord: Boolean
        get() = completedStorybooks.isNotEmpty() || completedChapters.isNotEmpty()
}

/**
 * 모달 '완성 스토리북' 섹션 항목
 * @param imageUrl 서버가 주는 커버 이미지 URL
 */
data class DateCompletedStorybook(
    val storybookId: Long,
    val title: String,
    val imageUrl: String? = null
)

/**
 * 모달 '장 기록중' 섹션 항목 — 그 날 완료한 장
 *
 * @param chapterOrder 그 날 완료한 마지막 장 순서(1~10). 표시("N장 기록을 완료했어요!")와 화면 이동에 모두 사용
 */
data class DateCompletedChapter(
    val storybookId: Long,
    val title: String,
    val chapterOrder: Int
)
