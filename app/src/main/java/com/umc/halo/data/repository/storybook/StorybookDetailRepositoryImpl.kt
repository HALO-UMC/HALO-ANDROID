package com.umc.halo.data.repository.storybook

import com.umc.halo.data.remote.api.storybook.StorybookDetailApi
import com.umc.halo.data.remote.dto.response.storybook.StorybookDetailResponse
import com.umc.halo.data.remote.dto.response.storybook.StorybookStartResponse
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
            ?: throw IllegalStateException("storybook data is null")
    }

    override suspend fun startStorybook(storybookId: Long): StorybookStartResponse {
        val response = storybookDetailApi.startStorybook(storybookId)

        return response.result
            ?: throw IllegalStateException("storybook is not started")
    }
}

private fun StorybookDetailResponse.toDomain() = StorybookDetailResult(
    storyBookId = storybookId,
    storyBookInfo = StoryBookInfo(
        title = title,
        storyBookIntro = description,
        imageUrl = imageUrl
    ),
    storyBookProgress = if (completedChapterCount == 10) StorybookProgress.Done else StorybookProgress.InProgress(completedChapterCount),
    storyBookIndex = chapters.map {
        StoryBookIndex(
            id = it.chapterOrder.toLong(),
            memberChapterId = it.memberChapterId,
            title = it.title,
            subTitle = it.shortDescription,
            description = it.description,
            imageUrl = it.shortImageUrl,
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

