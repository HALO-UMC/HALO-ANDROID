package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterCoverType
import com.umc.halo.domain.model.storybook.ChapterEmotion
import com.umc.halo.domain.model.storybook.ChapterSceneCard

enum class ChapterSceneRecordMethod {
    PHOTO,
    SCENE_CARD
}

enum class ChapterMood(
    val id: String
) {
    THANKFUL("GRATEFUL"),
    SAD("SAD"),
    THOUGHTFUL("THOUGHTFUL"),
    ANGRY("ANGRY"),
    AWKWARD("AWKWARD"),
    HAPPY("HAPPY");

    fun toEmotion(): ChapterEmotion =
        when (this) {
            THANKFUL -> ChapterEmotion.GRATEFUL
            SAD -> ChapterEmotion.SAD
            THOUGHTFUL -> ChapterEmotion.THOUGHTFUL
            ANGRY -> ChapterEmotion.ANGRY
            AWKWARD -> ChapterEmotion.AWKWARD
            HAPPY -> ChapterEmotion.HAPPY
        }

    companion object {
        fun fromEmotion(emotion: ChapterEmotion?): ChapterMood? =
            when (emotion) {
                ChapterEmotion.GRATEFUL -> THANKFUL
                ChapterEmotion.SAD -> SAD
                ChapterEmotion.THOUGHTFUL -> THOUGHTFUL
                ChapterEmotion.ANGRY -> ANGRY
                ChapterEmotion.AWKWARD -> AWKWARD
                ChapterEmotion.HAPPY -> HAPPY
                null -> null
            }
    }
}

data class ChapterProgressUiState(
    val isInitialized: Boolean = false,
    val chapter: Chapter? = null,
    val currentStep: ChapterProgressStep = ChapterProgressStep.INTRO,
    val questionAnswers: List<String> = listOf("", "", ""),
    val sceneCards: List<ChapterSceneCard> = emptyList(),
    val selectedSceneRecordMethod: ChapterSceneRecordMethod? = null,
    val selectedSceneImageUri: String? = null,
    val selectedSceneImageKey: String? = null,
    val isImageUploading: Boolean = false,
    val selectedSceneCardId: Long? = null,
    val pendingSceneCardId: Long? = null,
    val isSceneCardModalVisible: Boolean = false,
    val selectedMood: ChapterMood? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val navigateToStorybookDetail: Long? = null
) {
    val isFirstStep: Boolean
        get() = currentStep == ChapterProgressStep.INTRO

    val isQuestionStepNextEnabled: Boolean
        get() = questionAnswers.all { it.isNotBlank() }

    val selectedSceneCard: ChapterSceneCard?
        get() = sceneCards.firstOrNull { it.id == selectedSceneCardId }

    val pendingSceneCard: ChapterSceneCard?
        get() = sceneCards.firstOrNull { it.id == pendingSceneCardId }

    val isSceneSelected: Boolean
        get() = when (selectedSceneRecordMethod) {
            ChapterSceneRecordMethod.PHOTO ->
                !selectedSceneImageUri.isNullOrBlank() &&
                        !selectedSceneImageKey.isNullOrBlank() &&
                        !isImageUploading

            ChapterSceneRecordMethod.SCENE_CARD -> selectedSceneCardId != null
            null -> false
        }

    val isSceneStepNextEnabled: Boolean
        get() = isSceneSelected

    val isSceneCardConfirmEnabled: Boolean
        get() = pendingSceneCardId != null

    val isMoodStepNextEnabled: Boolean
        get() = selectedMood != null

    val coverType: ChapterCoverType?
        get() = when (selectedSceneRecordMethod) {
            ChapterSceneRecordMethod.PHOTO -> ChapterCoverType.IMAGE
            ChapterSceneRecordMethod.SCENE_CARD -> ChapterCoverType.SCENE_CARD
            null -> null
        }
}
