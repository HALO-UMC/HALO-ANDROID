package com.umc.halo.presentation.storybook.chapter

/**
 * 챕터 작성 화면에서 발생하는 사용자 이벤트
 */
sealed interface ChapterProgressUiEvent {

    data class Initialize(
        val storybookId: Long,
        val chapterId: Long
    ) : ChapterProgressUiEvent

    data class QuestionAnswerChanged(
        val index: Int,
        val answer: String
    ) : ChapterProgressUiEvent

    data class SceneRecordMethodSelected(
        val method: ChapterSceneRecordMethod
    ) : ChapterProgressUiEvent

    data class SceneImageSelected(
        val imageUri: String
    ) : ChapterProgressUiEvent

    data object NextClicked : ChapterProgressUiEvent

    data object BackClicked : ChapterProgressUiEvent
}