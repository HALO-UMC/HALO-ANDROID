package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter

/**
 * 챕터 작성 화면 전체에서 사용하는 UI 상태
 */
data class ChapterProgressUiState(
    val isInitialized: Boolean = false,
    val chapter: Chapter? = null,
    val currentStep: ChapterProgressStep = ChapterProgressStep.INTRO,

    // 질문 입력 화면의 답변 목록
    val answers: List<String> = emptyList()
) {
    val isFirstStep: Boolean
        get() = currentStep == ChapterProgressStep.INTRO

    /**
     * 질문 개수와 답변 개수가 같고,
     * 모든 질문에 공백이 아닌 답변이 입력됐는지 확인합니다.
     */
    val isQuestionCompleted: Boolean
        get() {
            val questions = chapter?.questions ?: return false

            return questions.isNotEmpty() &&
                    answers.size == questions.size &&
                    answers.all { answer -> answer.isNotBlank() }
        }

    /**
     * 현재 단계의 다음 버튼 활성화 여부입니다.
     */
    val isNextEnabled: Boolean
        get() = when (currentStep) {
            ChapterProgressStep.QUESTION -> isQuestionCompleted
            else -> true
        }
}