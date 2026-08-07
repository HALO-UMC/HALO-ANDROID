package com.umc.halo.data.remote.dto.request.chapter

data class ChapterSaveRequest(
    val chapterId: Long,
    val emotion: String?,
    val coverType: String?,
    val imageKey: String?,
    val sceneCardId: Long?,
    val answers: List<ChapterAnswerRequest>?,
    val status: String
)

data class ChapterAnswerRequest(
    val chapterQuestionId: Long,
    val answer: String
)

data class PresignedUrlRequest(
    val contentType: String,
    val fileSize: Long
)
