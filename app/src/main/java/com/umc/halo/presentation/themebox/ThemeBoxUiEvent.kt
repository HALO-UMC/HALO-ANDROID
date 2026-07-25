package com.umc.halo.presentation.themebox

import com.umc.halo.presentation.home.HomeUiEvent

sealed interface ThemeBoxUiEvent {
    data class OnContinueStoryBookClicked(val storyBookId: Long): ThemeBoxUiEvent
    data class OnCustomizedStoryBookClicked(val storyBookId: Long): ThemeBoxUiEvent
}