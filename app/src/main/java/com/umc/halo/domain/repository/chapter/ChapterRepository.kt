package com.umc.halo.domain.repository.chapter

import com.umc.halo.domain.model.storybook.ChapterSaveForm
import com.umc.halo.domain.model.storybook.ChapterSaveResult
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.domain.model.storybook.CompletedChapter
import com.umc.halo.domain.model.storybook.Chapter as StoryChapter

interface ChapterRepository {
    suspend fun getTodayChapter(
        storybookId: Long,
        chapterOrder: Int
    ): TodayChapter

    suspend fun saveMemberChapter(form: ChapterSaveForm): ChapterSaveResult

    suspend fun getCompletedChapter(memberChapterId: Long): CompletedChapter

    suspend fun uploadImageFromUri(imageUri: String): UploadedChapterImage
}

data class TodayChapter(
    val chapter: StoryChapter,
    val sceneCards: List<ChapterSceneCard>,
    val draft: com.umc.halo.domain.model.storybook.ChapterDraft
)

data class UploadedChapterImage(
    val imageKey: String
)
