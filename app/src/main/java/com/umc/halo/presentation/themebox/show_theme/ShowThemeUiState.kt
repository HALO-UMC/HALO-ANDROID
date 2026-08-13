package com.umc.halo.presentation.themebox.show_theme

import com.umc.halo.domain.model.showTheme.ThemeExhibitionChapter

data class ShowThemeUiState (
    val errorMessage: String? = null,
    val progress: Float = 0f,
    val currentPage: Int = 0,
    val isPlaying: Boolean = true,
    val storybookId: Long = 0,
    val chapters: List<ThemeExhibitionChapter> = emptyList()
)

