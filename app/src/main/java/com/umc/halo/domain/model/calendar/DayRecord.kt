package com.umc.halo.domain.model.calendar

/**
 * 날짜 클릭 시 모달에 표시할 해당 일의 기록
 * completedStorybook / completedChapters 가 모두 비어있으면 기록이 없는 모달로 표시
 */
data class DayRecord(
    val month: Int,
    val day: Int,
    val completedStorybook: DateCompletedStorybook? = null,          // 그 날 완성한 스토리북(있으면 먼저 표시)
    val completedChapters: List<DateCompletedChapter> = emptyList()  // 그 날 완료한 장(최신순)
) {
    val hasRecord: Boolean
        get() = completedStorybook != null || completedChapters.isNotEmpty()
}

/** 모달 '완성 스토리북' 섹션 항목 */
data class DateCompletedStorybook(
    val storybookId: Long,
    val title: String
)

/**
 * 모달 '장 기록중' 섹션 항목
 * 그 날에 완료한 장임
 *
 * [chapter] 는 화면에 보여줄 '몇 번째 장'인지(1~10)
 * [chapterId] 는 그 장의 고유 id — 장 완료 결과 화면(chapter_result)으로 이동할 때 필요
 */
data class DateCompletedChapter(
    val storybookId: Long,
    val chapterId: Long,  // 장 고유 id (이동용)
    val title: String,
    val chapter: Int      // 그 날 완료한 장 번호 (표시용)
)
