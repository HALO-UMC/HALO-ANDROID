package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.home.CurrentProgress
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.domain.model.themebox.Theme

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




