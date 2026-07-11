package com.umc.halo.presentation.home

sealed interface HomeUiEvent {
    data class OnCustomizedStoryBookClicked(val storyBookId: Int): HomeUiEvent
    data class OnBookClicked(val storyBookId: Int): HomeUiEvent
    data class OnContinueStoryBookClicked(
        val storyBookId: Int,
        val chapterId: Int,
    ): HomeUiEvent
}