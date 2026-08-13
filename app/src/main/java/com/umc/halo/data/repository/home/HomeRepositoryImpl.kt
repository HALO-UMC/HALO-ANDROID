package com.umc.halo.data.repository.home

import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.api.home.HomeApi
import com.umc.halo.data.remote.dto.response.home.HomeResponse
import com.umc.halo.domain.model.home.BookResult
import com.umc.halo.domain.model.home.HomeResult
import com.umc.halo.domain.model.home.HomeStatus
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.domain.repository.home.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
): HomeRepository {
    override suspend fun getHome(): HomeResult {
        val response = runCatching { homeApi.getHome() }
            .getOrElse { throwable ->
                throw IllegalStateException(throwable.toApiErrorMessage(LOAD_FAILED_MESSAGE))
            }

        return response.result?.toDomain()
            ?: error(response.toApiErrorMessage(LOAD_FAILED_MESSAGE))
    }

    private companion object {
        const val LOAD_FAILED_MESSAGE = "홈 정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}

// Response 데이터 가공
private fun HomeResponse.toDomain() = HomeResult(
    homeStatus = HomeStatus.from(homeStatus),
    memberName = memberName,
    continueStorybookList = inProgressStorybooks.map {
        ContinueStorybook(
            storybookId = it.storybookId,
            title = it.title,
            currentChapterOrder = it.currentChapterOrder.toLong(),
            todayAvailable = it.todayAvailable
        )
    },
    bookShelfList = bookshelf.map {
        BookResult(
            storybookId = it.storybookId,
            title = it.title,
            shortDescription = it.shortDescription,
            imageUrl = it.imageUrl,
            currentChapterOrder = it.currentChapterOrder ?: 0,
            todayAvailable = it.todayAvailable,
            isFirst = it.currentChapterOrder == null,
            recommendationReasonText = it.recommendationReasonText
        )
    },
    customStorybookList = recommendedStorybooks.map {
        CustomStorybook(
            id = it.storybookId,
            tag = it.recommendationReasonText,
            title = it.title,
            subtitle = it.shortDescription,
            imageUrl = it.imageUrl
        )
    }
)