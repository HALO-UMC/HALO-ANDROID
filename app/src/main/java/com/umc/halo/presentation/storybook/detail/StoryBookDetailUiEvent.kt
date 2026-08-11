package com.umc.halo.presentation.storybook.detail

import com.umc.halo.presentation.storybook.list.StorybookUiEvent

sealed interface StoryBookDetailUiEvent {
    data class OnClickTodayStoryBook(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent
    data class OnClickStoryBookIndex(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent

    data object OnClickOpenDialog: StoryBookDetailUiEvent
    data object OnClickDismissDialog: StoryBookDetailUiEvent

    data object OnclickBackArrow: StoryBookDetailUiEvent
    data object OnScreenShown : StoryBookDetailUiEvent
    data object OnRetryClicked : StoryBookDetailUiEvent
    data object ErrorShown : StoryBookDetailUiEvent
}