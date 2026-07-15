package com.umc.halo.presentation.storybook.index

sealed interface StoryBookDetailUiEvent {
    data class OnClickTodayStoryBook(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent
    data class OnClickStoryBookIndex(val storyBookId: Long, val chapterId: Long): StoryBookDetailUiEvent
}