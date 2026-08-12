package com.umc.halo.data.remote.dto.response.storybook

data class StorybookDetailResponse(
    val storybookId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val completedChapterCount: Int,
    val chapters: List<Chapter>
)

data class Chapter(
    val memberChapterId: Long?,
    val chapterOrder: Int,
    val title: String,
    val shortImageUrl: String,
    val longImageUrl: String,
    val shortDescription: String,
    val description: String,
    val status: String
)

data class StorybookStartResponse(
    val memberStorybookId: Long,
    val storybookId: Long,
    val status: String
)
