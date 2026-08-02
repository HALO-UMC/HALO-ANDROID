package com.umc.halo.data.remote.dto.response.storybook

data class StorybookDetailResponse(
    val storybookId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val completedChapter: Int,
    val chapters: List<Chapter>
)

data class Chapter(
    val chapterOrder: Int,
    val title: String,
    val imageUrl: String,
    val shortDescription: String,
    val description: String,
    val status: String
)