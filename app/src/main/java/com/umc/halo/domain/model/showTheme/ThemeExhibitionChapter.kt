package com.umc.halo.domain.model.showTheme

data class ThemeExhibitionResult(
    val storybookId: Long = 0,
    val chapters: List<ThemeExhibitionChapter>
)
data class ThemeExhibitionChapter(
    val id: Int = 0,
    val title: String = "void",
    val imageUrl: String = "void",
    val completedDate: String = "void",
    val summary: String = "void"
)