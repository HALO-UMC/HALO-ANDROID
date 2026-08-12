package com.umc.halo.presentation.themebox.show_theme

import com.umc.halo.data.remote.dto.response.themebox.ThemeExhibitionChapterResponse

data class ShowThemeUiState (
    val errorMessage: String? = null,
    val progress: Float = 0f,
    val currentPage: Int = 0,
    val isPlaying: Boolean = true,
    val storybookId: Long = 0,
    val chapters: List<ThemeExhibitionChapter> = emptyList()
)

data class ThemeExhibitionChapter(
    val id: Int = 0,
    val title: String = "void",
    val imageUrl: String = "void",
    val completedDate: String = "void",
    val summary: String = "void"
)