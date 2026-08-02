package com.umc.halo.data.repository.storybook

import com.umc.halo.data.remote.api.storybook.StorybookDetailApi
import com.umc.halo.data.remote.dto.response.storybook.StorybookDetailResponse
import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookDetailResult
import com.umc.halo.domain.model.storybook.StorybookIndexStatus
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.TodayStoryBook
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import com.umc.halo.presentation.storybook.detail.StoryBookProgress
import javax.inject.Inject

class StorybookDetailRepositoryImpl @Inject constructor(
    private val storybookDetailApi: StorybookDetailApi
): StorybookDetailRepository {
    override suspend fun getStorybookDetail(storybookId: Long): StorybookDetailResult {
        val response = storybookDetailApi.getStorybookDetail(storybookId)

        return response.result?.toDomain()
            ?: throw IllegalStateException("Home data is null")
    }
}

private fun StorybookDetailResponse.toDomain() = StorybookDetailResult(
    storyBookId = storybookId,
    storyBookInfo = StoryBookInfo(
        title = title,
        storyBookIntro = description,
        imageUrl = imageUrl
    ),
    storyBookProgress = if (completedChapter == 10) StorybookProgress.Done else StorybookProgress.InProgress(completedChapter),
    storyBookIndex = chapters.map {
        StoryBookIndex(
            id = it.chapterOrder.toLong(),
            title = it.title,
            subTitle = it.shortDescription,
            description = it.description,
            imageUrl = it.imageUrl,
            isLocked = lockedStatusStringIntoBoolean(it.status),
            isCompleted = completeStatusStringIntoBoolean(it.status)
        )
    }
)

private fun lockedStatusStringIntoBoolean(status: String): Boolean {
    return when (StorybookIndexStatus.from(status)) {
        StorybookIndexStatus.LOCKED -> true
        StorybookIndexStatus.TODAY_LOCKED -> true
        StorybookIndexStatus.COMPLETED -> false
        StorybookIndexStatus.TODAY -> false
    }
}

private fun completeStatusStringIntoBoolean(status: String): Boolean {
    return when (StorybookIndexStatus.from(status)) {
        StorybookIndexStatus.LOCKED -> false
        StorybookIndexStatus.TODAY_LOCKED -> false
        StorybookIndexStatus.COMPLETED -> true
        StorybookIndexStatus.TODAY -> false
    }
}