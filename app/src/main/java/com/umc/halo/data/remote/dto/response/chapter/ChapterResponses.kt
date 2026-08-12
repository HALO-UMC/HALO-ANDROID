package com.umc.halo.data.remote.dto.response.chapter

import com.google.gson.annotations.SerializedName

data class TodayChapterResponse(
    val storybookTitle: String?,
    val chapterId: Long?,
    @SerializedName(value = "chapterTitle", alternate = ["chatperTitle"])
    val chapterTitle: String?,
    val chapterOrder: Int?,
    val longImageUrl: String?,
    val description: String?,
    val guide: String?,
    val character: ChapterCharacterResponse?,
    val questions: List<ChapterQuestionResponse>?,
    val sceneCards: List<ChapterSceneCardResponse>?,
    val draft: ChapterDraftResponse?
)

data class ChapterCharacterResponse(
    val writingCharacterImageUrl: String?,
    val sceneCardCharacterImageUrl: String?
)

data class ChapterQuestionResponse(
    val chapterQuestionId: Long?,
    val questionOrder: Int?,
    val question: String?
)

data class ChapterSceneCardResponse(
    val sceneCardId: Long?,
    val imageUrl: String?
)

data class ChapterDraftResponse(
    val status: String?,
    val answers: List<ChapterDraftAnswerResponse>?,
    val coverType: String?,
    val imageUrl: String?,
    val imageKey: String?,
    val sceneCardId: Long?,
    val emotion: String?
)

data class ChapterDraftAnswerResponse(
    val chapterQuestionId: Long?,
    val questionOrder: Int?,
    val answer: String?
)

data class ChapterSaveResponse(
    val memberChapterId: Long?,
    val isStorybookCompleted: Boolean?
)

data class CompletedChapterResponse(
    val memberChapterId: Long?,
    val storybookTitle: String?,
    val chapterTitle: String?,
    val chapterOrder: Int?,
    val description: String?,
    val emotion: String?,
    val coverType: String?,
    val imageUrl: String?,
    val sceneCardImageUrl: String?,
    val answers: List<CompletedChapterAnswerResponse>?,
    val completedDate: String?,
    val chapterImageUrl: String?
)

data class CompletedChapterAnswerResponse(
    val question: String?,
    val answer: String?
)

data class PresignedUrlResponse(
    val presignedUrl: String?,
    val imageKey: String?,
    val requiredHeaders: Map<String, String>?,
    @SerializedName(value = "expires", alternate = ["expries"])
    val expires: Int?
)
