package com.umc.halo.presentation.storybook.chapter

/**
 * 챕터 작성 화면에서 발생하는 사용자 이벤트
 */
sealed interface ChapterProgressUiEvent {

    /**
     * NavGraph에서 전달받은 ID로 챕터 데이터를 초기화합니다.
     */
    data class Initialize(
        val storybookId: Long,
        val chapterId: Long
    ) : ChapterProgressUiEvent

    /**
     * 질문 답변 변경
     */
    data class AnswerChanged(
        val questionIndex: Int,
        val answer: String
    ) : ChapterProgressUiEvent

    /**
     * 하단의 다음 버튼 클릭
     */
    data object NextClicked : ChapterProgressUiEvent

    /**
     * 첫 단계를 제외한 내부 단계에서 이전 버튼 클릭
     */
    data object BackClicked : ChapterProgressUiEvent
}