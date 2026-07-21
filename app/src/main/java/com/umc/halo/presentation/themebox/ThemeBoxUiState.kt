package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.home.CurrentProgress
import com.umc.halo.domain.model.storybook.CustomStorybook

sealed interface ThemeBoxUiState{
    data class Filled(
        val numberOfCharacter: Int = 0,
        val storyBookInProgress: Int = 0,
        val themeList: List<Theme> = emptyList()
    ): ThemeBoxUiState

    sealed interface Empty : ThemeBoxUiState {
        data class RU(
            val continueStorybookList: List<ContinueStorybook> = emptyList()
        ): Empty

        data class FTU(
            val customStorybookList: List<CustomStorybook> = emptyList()
        ): Empty
    }
}


data class Theme(
    val character: String,
    val title: String,
    val subTitle: String
)

data class ContinueStorybook(
    val title: String,
    val theme: Int,
    val chapter: Int
)
