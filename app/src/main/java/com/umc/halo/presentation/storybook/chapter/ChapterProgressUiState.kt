package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterSceneCard

enum class ChapterSceneRecordMethod {
    PHOTO,
    SCENE_CARD
}

enum class ChapterMood(
    val id: String
) {
    THANKFUL("thankful"),
    SAD("sad"),
    THOUGHTFUL("thoughtful"),
    ANGRY("angry"),
    AWKWARD("awkward"),
    HAPPY("happy")
}

/**
 * 챕터 작성 화면 전체에서 사용하는 UI 상태
 */
data class ChapterProgressUiState(
    val isInitialized: Boolean = false,
    val chapter: Chapter? = null,
    val currentStep: ChapterProgressStep = ChapterProgressStep.INTRO,

    // 질문 입력 답변
    val questionAnswers: List<String> = listOf("", "", ""),

    // 장면카드 후보 목록
    val sceneCards: List<ChapterSceneCard> = emptyList(),

    // 장면 남기기 방식 선택
    val selectedSceneRecordMethod: ChapterSceneRecordMethod? = null,

    // 갤러리에서 선택한 이미지 Uri 문자열
    val selectedSceneImageUri: String? = null,

    // 최종 선택 완료된 장면카드 id
    val selectedSceneCardId: Long? = null,

    // 장면카드 모달에서 임시로 선택 중인 카드 id
    val pendingSceneCardId: Long? = null,

    // 장면카드 선택 모달 표시 여부
    val isSceneCardModalVisible: Boolean = false,

    // 이번 장을 기록하며 선택한 감정
    val selectedMood: ChapterMood? = null
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
            ChapterSceneRecordMethod.PHOTO -> !selectedSceneImageUri.isNullOrBlank()
            ChapterSceneRecordMethod.SCENE_CARD -> selectedSceneCardId != null
            null -> false
        }

    val isSceneStepNextEnabled: Boolean
        get() = isSceneSelected

    val isSceneCardConfirmEnabled: Boolean
        get() = pendingSceneCardId != null

    val isMoodStepNextEnabled: Boolean
        get() = selectedMood != null
}
