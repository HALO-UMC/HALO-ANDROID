package com.umc.halo.domain.model.home

import com.umc.halo.data.remote.dto.response.home.BookshelfResponse
import com.umc.halo.domain.model.home.HomeStatus.NO_STORYBOOK
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook

/**
 * API 응답 처리를 위한 model
 */
data class HomeResult(
    val homeStatus: HomeStatus,
    val memberName: String,
    val continueStorybookList: List<ContinueStorybook>,
    val bookShelfList: List<Bookshelf>,
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

data class Bookshelf(
    val storybookId: Long,
    val title: String,
    val themeOrder: Int,
    val spineColor: String?,
    val status: BookStatus
)

enum class BookStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): BookStatus =
            entries.find { it.name == value } ?: NOT_STARTED
    }
}