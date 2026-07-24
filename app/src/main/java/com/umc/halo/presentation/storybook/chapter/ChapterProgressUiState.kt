package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter

enum class ChapterSceneRecordMethod {
    PHOTO,
    SCENE_CARD
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

    // 장면 남기기 방식 선택
    val selectedSceneRecordMethod: ChapterSceneRecordMethod? = null,

    // 갤러리에서 선택한 이미지 Uri 문자열
    val selectedSceneImageUri: String? = null,

    // 나중에 장면카드 구현 시 사용할 값
    val selectedSceneCardId: Long? = null
) {
    val isFirstStep: Boolean
        get() = currentStep == ChapterProgressStep.INTRO

    val isQuestionStepNextEnabled: Boolean
        get() = questionAnswers.all { it.isNotBlank() }

    /**
     * 현재 선택된 방식에 맞는 실제 선택 결과가 있는지 확인합니다.
     *
     * PHOTO 상태라면 selectedSceneImageUri가 있어야 하고,
     * SCENE_CARD 상태라면 selectedSceneCardId가 있어야 합니다.
     */
    val isSceneSelected: Boolean
        get() = when (selectedSceneRecordMethod) {
            ChapterSceneRecordMethod.PHOTO -> !selectedSceneImageUri.isNullOrBlank()
            ChapterSceneRecordMethod.SCENE_CARD -> selectedSceneCardId != null
            null -> false
        }

    val isSceneStepNextEnabled: Boolean
        get() = isSceneSelected
}