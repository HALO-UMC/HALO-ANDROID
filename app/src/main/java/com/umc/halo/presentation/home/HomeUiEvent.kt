package com.umc.halo.presentation.home

import com.umc.halo.presentation.storybook.list.StorybookUiEvent

sealed interface HomeUiEvent {
    data class OnCustomizedStoryBookClicked(val storyBookId: Long): HomeUiEvent
    data class OnBookClicked(val storyBookId: Long?): HomeUiEvent
    data class OnStartStorybookClicked(val storyBookId: Long): HomeUiEvent
    data class OnContinueStoryBookClicked(val storyBookId: Long): HomeUiEvent
    data object ErrorShown: HomeUiEvent
    data object OnScreenShown : HomeUiEvent
    data object OnRetryClicked : HomeUiEvent
}