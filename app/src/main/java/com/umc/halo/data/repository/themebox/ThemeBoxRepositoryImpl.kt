package com.umc.halo.data.repository.themebox

import com.umc.halo.data.remote.api.themebox.ThemeBoxApi
import com.umc.halo.data.remote.dto.response.themebox.ThemeBoxResponse
import com.umc.halo.data.remote.dto.response.themebox.ThemeExhibitionResponse
import com.umc.halo.domain.model.showTheme.ThemeExhibitionChapter
import com.umc.halo.domain.model.showTheme.ThemeExhibitionResult
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.domain.model.themebox.ThemeBoxResult
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import javax.inject.Inject

class ThemeBoxRepositoryImpl @Inject constructor(
    val themeBoxApi: ThemeBoxApi
): ThemeBoxRepository {
    override suspend fun getThemeBox(): ThemeBoxResult {
        val response = themeBoxApi.getThemeBox()

        return response.result?.toDomain()
            ?: throw IllegalStateException("themeBox data is null")
    }

    private fun ThemeBoxResponse.toDomain() = ThemeBoxResult(
        numberOfCharacter = stats.collectedCharacterCount,
        storyBookInProgress = stats.inProgressStorybookCount,
        currentStorybookId = currentStorybookId,
        themeList = storybooks.map {
            Theme(
                storybookId = it.storybookId,
                character = it.characterName,
                title = it.title,
                subTitle = it.summary,
                imageUrl = it.characterImageUrl
            )
        },
        continueStorybookList = inProgressStorybooks.map {
            ContinueStorybook(
                title = it.title,
                storybookId = it.storybookId,
                currentChapterOrder = it.nextChapterOrder.toLong(),
                todayAvailable = it.todayAvailable
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

    override suspend fun getThemeExhibition(storybookId: Long): ThemeExhibitionResult {
        val response = themeBoxApi.getThemeExhibition(storybookId)

        return response.result?.toDomain()
            ?: throw IllegalStateException("theme exhibition data is null")
    }

    private fun ThemeExhibitionResponse.toDomain() = ThemeExhibitionResult(
        storybookId = storybookId,
        chapters = chapters.map {
            ThemeExhibitionChapter(
                id = it.chapterOrder,
                title = it.title,
                imageUrl = it.chapterImageUrl,
                completedDate = it.completedDate,
                summary = it.summary
            )
        }
    )
}