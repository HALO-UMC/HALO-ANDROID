package com.umc.halo.presentation.storybook.detail

import com.umc.halo.domain.model.home.CurrentProgress

data class StoryBookDetailUiState(
    val storyBookId: Long = 0,
    val storyBookInfo: StoryBookInfo = StoryBookInfo(
        title = "void",
        storyBookIntro = "void"
    ),
    val storyBookProgress: CurrentProgress = CurrentProgress(
        theme = 0,
        chapter = 0
    ),
    val todayStoryBookInfo: TodayStoryBook = TodayStoryBook(
        id = 1,
        title = "나와 같은 나이었던 시절",
        tag = "지금의 내 나이였을 때 부모님은 어떤 하루를 살고 있었는지 들어봅시다.",
        isLocked = false
    ),
    val storyBookIndex: List<StoryBookIndex> = emptyList()
)

data class StoryBookInfo(
    val title: String,
    val storyBookIntro: String
)

data class StoryBookIndex(
    val id: Long,
    val title: String,
    val subTitle: String,
    val isLocked: Boolean
)

data class TodayStoryBook(
    val id: Long,
    val title: String,
    val tag: String,
    val isLocked: Boolean
)