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

    // 질문 페이지에서 이미 추가한 값들이 있다면 그대로 유지하면 됨
    val questionAnswers: List<String> = listOf("", "", ""),

    // 장면 남기기 방식 선택
    val selectedSceneRecordMethod: ChapterSceneRecordMethod? = null,

    // 나중에 사진/장면카드 선택 완료되면 true로 변경
    val isSceneImageSelected: Boolean = false
) {
    val isFirstStep: Boolean
        get() = currentStep == ChapterProgressStep.INTRO

    val isQuestionStepNextEnabled: Boolean
        get() = questionAnswers.all { it.isNotBlank() }

    val isSceneStepNextEnabled: Boolean
        get() = isSceneImageSelected
}