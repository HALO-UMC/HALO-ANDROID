package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter

/**
 * 챕터 작성 화면 전체에서 사용하는 UI 상태
 *
 * 질문 답변, 이미지, 감정 등의 값은 해당 화면을 구현하면서
 * 이 UiState에 순서대로 추가할 예정입니다.
 */
data class ChapterProgressUiState(
    val isInitialized: Boolean = false,
    val chapter: Chapter? = null,
    val currentStep: ChapterProgressStep = ChapterProgressStep.INTRO
) {
    val isFirstStep: Boolean
        get() = currentStep == ChapterProgressStep.INTRO
}