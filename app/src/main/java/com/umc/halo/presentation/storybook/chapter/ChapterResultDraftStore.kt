package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterSceneCard

data class ChapterResultDraft(
    val chapter: Chapter,
    val questions: List<String>,
    val answers: List<String>,
    val selectedSceneCard: ChapterSceneCard?,
    val selectedImageUri: String?,
    val mood: ChapterMood
)

/**
 * API 저장 연동 전까지 작성 완료 직후 결과 화면에 방금 입력한 내용을 넘기는 임시 저장소.
 * 서버 연동 후에는 완료 챕터 조회 API 결과로 교체하면 됩니다.
 */
object ChapterResultDraftStore {
    private val drafts = mutableMapOf<String, ChapterResultDraft>()

    fun save(state: ChapterProgressUiState) {
        val chapter = state.chapter ?: return
        val mood = state.selectedMood ?: return

        drafts[key(chapter.storybookId, chapter.id)] = ChapterResultDraft(
            chapter = chapter,
            questions = chapter.questions,
            answers = state.questionAnswers,
            selectedSceneCard = state.selectedSceneCard,
            selectedImageUri = state.selectedImageUri,
            mood = mood
        )
    }

    fun get(storybookId: Long, chapterId: Long): ChapterResultDraft? =
        drafts[key(storybookId, chapterId)]

    private fun key(storybookId: Long, chapterId: Long): String =
        "$storybookId:$chapterId"
}
