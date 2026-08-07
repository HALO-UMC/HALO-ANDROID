package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.ChapterCoverType
import com.umc.halo.domain.model.storybook.CompletedChapter

data class ChapterResultUiState(
    val isLoading: Boolean = false,
    val completedChapter: CompletedChapter? = null,
    val errorMessage: String? = null
) {
    val selectedMood: ChapterMood
        get() = ChapterMood.fromEmotion(completedChapter?.emotion) ?: ChapterMood.THANKFUL

    val sceneImageUrl: String?
        get() = when (completedChapter?.coverType) {
            ChapterCoverType.IMAGE -> completedChapter.imageUrl
            ChapterCoverType.SCENE_CARD -> completedChapter.sceneCardImageUrl
            null -> null
        }
}
