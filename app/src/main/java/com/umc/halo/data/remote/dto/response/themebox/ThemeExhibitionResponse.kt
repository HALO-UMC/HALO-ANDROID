package com.umc.halo.data.remote.dto.response.themebox
data class ThemeExhibitionResponse(
    val storybookId: Long,
    val chapters: List<ThemeExhibitionChapterResponse>
)

data class ThemeExhibitionChapterResponse(
    val chapterOrder: Int,
    val chapterImageUrl: String,
    val title: String,
    val summary: String,
    val completedDate: String,
    val emotion: String
)