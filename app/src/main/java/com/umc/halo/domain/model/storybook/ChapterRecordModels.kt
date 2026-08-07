package com.umc.halo.domain.model.storybook

data class ChapterDraft(
    val status: ChapterDraftStatus,
    val answers: List<ChapterDraftAnswer>,
    val coverType: ChapterCoverType?,
    val imageUrl: String?,
    val imageKey: String?,
    val sceneCardId: Long?,
    val emotion: ChapterEmotion?
)

data class ChapterDraftAnswer(
    val chapterQuestionId: Long,
    val questionOrder: Int,
    val answer: String
)

enum class ChapterDraftStatus {
    NONE,
    DRAFT;

    companion object {
        fun from(value: String?): ChapterDraftStatus =
            entries.find { it.name == value } ?: NONE
    }
}

enum class ChapterCoverType {
    IMAGE,
    SCENE_CARD;

    companion object {
        fun from(value: String?): ChapterCoverType? =
            entries.find { it.name == value }
    }
}

enum class ChapterEmotion {
    GRATEFUL,
    SAD,
    THOUGHTFUL,
    ANGRY,
    AWKWARD,
    HAPPY;

    companion object {
        fun from(value: String?): ChapterEmotion? =
            entries.find { it.name == value }
    }
}

enum class ChapterSaveStatus {
    DRAFT,
    COMPLETED
}

data class ChapterSaveForm(
    val chapterId: Long,
    val emotion: ChapterEmotion?,
    val coverType: ChapterCoverType?,
    val imageKey: String?,
    val sceneCardId: Long?,
    val answers: List<ChapterSaveAnswer>,
    val status: ChapterSaveStatus
)

data class ChapterSaveAnswer(
    val chapterQuestionId: Long,
    val answer: String
)

data class ChapterSaveResult(
    val memberChapterId: Long,
    val isStorybookCompleted: Boolean
)

data class CompletedChapter(
    val memberChapterId: Long,
    val storybookTitle: String,
    val chapterTitle: String,
    val chapterOrder: Int,
    val description: String,
    val emotion: ChapterEmotion,
    val coverType: ChapterCoverType,
    val imageUrl: String?,
    val sceneCardImageUrl: String?,
    val answers: List<CompletedChapterAnswer>,
    val completedDate: String,
    val chapterImageUrl: String
)

data class CompletedChapterAnswer(
    val question: String,
    val answer: String
)
