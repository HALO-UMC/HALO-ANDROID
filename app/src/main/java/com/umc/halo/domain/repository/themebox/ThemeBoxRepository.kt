package com.umc.halo.domain.repository.themebox

import com.umc.halo.domain.model.showTheme.ThemeExhibitionResult
import com.umc.halo.domain.model.themebox.ThemeBoxResult

interface ThemeBoxRepository {
    suspend fun getThemeBox(): ThemeBoxResult
    suspend fun getThemeExhibition(storybookId: Long): ThemeExhibitionResult
}