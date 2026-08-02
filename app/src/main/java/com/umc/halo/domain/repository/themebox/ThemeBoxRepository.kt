package com.umc.halo.domain.repository.themebox

import com.umc.halo.data.remote.dto.response.themebox.ThemeExhibitionResponse
import com.umc.halo.domain.model.themebox.ThemeBoxResult

interface ThemeBoxRepository {
    suspend fun getThemeBox(): ThemeBoxResult
    suspend fun getThemeExhibition(storybookId: Long): ThemeExhibitionResponse
}