package com.umc.halo.presentation.storybook.detail

import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.TodayStoryBook

data class StoryBookDetailUiState(
    val isLoading: Boolean = false,
    val hasLoadFailed: Boolean = false,
    val errorMessage: String? = null,
    val storyBookId: Long = 0,
    val storyBookInfo: StoryBookInfo = StoryBookInfo(
        title = "void",
        storyBookIntro = "void",
        imageUrl = ""
    ),
    val storyBookProgress: StorybookProgress = StorybookProgress.InProgress(1),
    val todayStoryBookInfo: TodayStoryBook = TodayStoryBook(
        id = 1,
        title = "나와 같은 나이었던 시절",
        tag = "지금의 내 나이였을 때 부모님은 어떤 하루를 살고 있었는지 들어봅시다.",
        isLocked = false,
        isCompleted = true,
        imageUrl = ""
    ),
    val storyBookIndex: List<StoryBookIndex> = emptyList(),
    val showDialog: Boolean = false,
    val startedStorybook: Long? = null
)

