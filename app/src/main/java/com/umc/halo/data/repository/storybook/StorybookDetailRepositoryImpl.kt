package com.umc.halo.data.repository.storybook

import com.umc.halo.data.remote.api.storybook.StorybookDetailApi
import com.umc.halo.core.network.BaseResponse
import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.dto.response.storybook.StorybookDetailResponse
import com.umc.halo.data.remote.dto.response.storybook.StorybookStartResponse
import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookDetailResult
import com.umc.halo.domain.model.storybook.StorybookIndexStatus
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.StorybookStartResult
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import javax.inject.Inject

class StorybookDetailRepositoryImpl @Inject constructor(
    private val storybookDetailApi: StorybookDetailApi
): StorybookDetailRepository {
    override suspend fun getStorybookDetail(storybookId: Long): StorybookDetailResult {
        val response = storybookDetailApi.getStorybookDetail(storybookId)

        if (!response.isSuccess) {
            error(response.toApiErrorMessage("스토리북 상세 페이지를 불러오지 못했어요."))
        }

        return response.result?.toDomain()
            ?: throw IllegalStateException("스토리북 상세 페이지를 불러오지 못했어요.")
    }

    override suspend fun startStorybook(storybookId: Long): StorybookStartResult {
        val response = storybookDetailApi.startStorybook(storybookId)

        if (!response.isSuccess) {
            error(response.toApiErrorMessage("스토리북을 시작하지 못했어요."))
        }

        return response.result?.toDomain()
            ?: throw IllegalStateException("스토리북을 시작하지 못했어요.")
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

private fun StorybookStartResponse.toDomain() = StorybookStartResult(
    memberStorybookId = memberStorybookId,
    storybookId = storybookId,
    status = status
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

