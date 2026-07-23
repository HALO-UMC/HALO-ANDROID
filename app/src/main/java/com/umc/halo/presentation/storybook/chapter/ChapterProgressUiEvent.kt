package com.umc.halo.presentation.storybook.chapter

sealed interface ChapterProgressUiEvent {

    data class Initialize(
        val storybookId: Long,
        val chapterId: Long
    ) : ChapterProgressUiEvent

    data object NextClicked : ChapterProgressUiEvent

    data object BackClicked : ChapterProgressUiEvent

    data class QuestionAnswerChanged(
        val index: Int,
        val answer: String
    ) : ChapterProgressUiEvent

    data class SceneRecordMethodSelected(
        val method: ChapterSceneRecordMethod
    ) : ChapterProgressUiEvent
}