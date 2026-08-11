package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.domain.model.themebox.Theme

data class ThemeBoxUiState(
    val isLoading: Boolean = false,
    val hasLoadFailed: Boolean = false,
    val errorMessage: String? = null,

    val themeBoxState: ThemeBoxState = ThemeBoxState.Empty.FTU,
    val numberOfCharacter: Int = 0,
    val storyBookInProgress: Int = 0,
    val themeList: List<Theme> = emptyList(),
    val currentStorybookId: Long? = null,
    val continueStorybookList: List<ContinueStorybook> = emptyList(),
    val customStorybookList: List<CustomStorybook> = emptyList()
)

sealed interface ThemeBoxState {

    data object Filled : ThemeBoxState

    sealed interface Empty : ThemeBoxState {

        data object RU : Empty

        data object FTU : Empty
    }
}




