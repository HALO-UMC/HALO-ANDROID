package com.umc.halo.domain.model.home

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook

/**
 * API 응답 처리를 위한 model
 */
data class HomeResult(
    val homeStatus: HomeStatus,
    val memberName: String,
    val continueStorybookList: List<ContinueStorybook>,
    val bookShelfList: List<BookResult>,
    val customStorybookList: List<CustomStorybook>
)

enum class HomeStatus {
    NO_STORYBOOK,
    IN_PROGRESS,
    MULTIPLE_IN_PROGRESS,
    ALL_COMPLETED_TODAY;

    companion object {
        fun from(value: String): HomeStatus =
            entries.find { it.name == value } ?: NO_STORYBOOK
    }
}

data class BookResult(
    val storybookId: Long,
    val title: String,
    val shortDescription: String,
    val imageUrl: String,
    val currentChapterOrder: Int,
    val todayAvailable: Boolean,
    val recommendationReasonText: String?,
    val isFirst: Boolean
)