package com.umc.halo.presentation.storybook.detail

sealed interface StoryBookDetailUiEvent {
    data class OnClickTodayStoryBook(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent
    data class OnClickStoryBookIndex(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent
}