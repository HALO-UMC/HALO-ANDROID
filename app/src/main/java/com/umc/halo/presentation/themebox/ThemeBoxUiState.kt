package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.storybook.CustomStorybook

sealed interface ThemeBoxUiState{
    data class Filled(
        val numberOfCharacter: Int = 0,
        val storyBookInProgress: Int = 0,
        val themeList: List<Theme> = emptyList()
    ): ThemeBoxUiState

    sealed interface Empty : ThemeBoxUiState {
        data class RU(
            val storyBookInProgress: Int = 0
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
