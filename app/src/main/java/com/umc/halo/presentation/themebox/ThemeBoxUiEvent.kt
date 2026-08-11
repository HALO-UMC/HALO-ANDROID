package com.umc.halo.presentation.themebox

import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.storybook.list.StorybookUiEvent

sealed interface ThemeBoxUiEvent {
    data class OnContinueStoryBookClicked(val storyBookId: Long): ThemeBoxUiEvent
    data class OnCustomizedStoryBookClicked(val storyBookId: Long): ThemeBoxUiEvent
    data class OnShowThemeClicked(val storyBookId: Long): ThemeBoxUiEvent
    data class OnPagerChanged(val page: Int): ThemeBoxUiEvent
    data object OnThemeBoxShown: ThemeBoxUiEvent
    data object OnRetryClicked : ThemeBoxUiEvent
    data object ErrorShown : ThemeBoxUiEvent
}