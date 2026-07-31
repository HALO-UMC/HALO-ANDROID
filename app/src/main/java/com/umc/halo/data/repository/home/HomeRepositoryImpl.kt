package com.umc.halo.data.repository.home

import com.umc.halo.data.remote.api.home.HomeApi
import com.umc.halo.data.remote.dto.response.home.HomeResponse
import com.umc.halo.domain.model.home.BookStatus
import com.umc.halo.domain.model.home.Bookshelf
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
        val response = homeApi.getHome()

        return response.result?.toDomain()
            ?: throw IllegalStateException("Home data is null")
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
            currentChapterOrder = it.currentChapterOrder.toLong()
        )
    },
    bookShelfList = bookshelf.map {
        Bookshelf(
            storybookId = it.storybookId,
            title = it.title,
            themeOrder = it.themeOrder,
            spineColor = it.spineColor,
            status = BookStatus.from(it.status)
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