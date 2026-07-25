package com.umc.halo.presentation.home

sealed interface HomeUiEvent {
    data class OnCustomizedStoryBookClicked(val storyBookId: Long): HomeUiEvent
    data class OnBookClicked(val storyBookId: Long?): HomeUiEvent
    data class OnStartStorybookClicked(val storyBookId: Long): HomeUiEvent
    data class OnContinueStoryBookClicked(val storyBookId: Long): HomeUiEvent
}