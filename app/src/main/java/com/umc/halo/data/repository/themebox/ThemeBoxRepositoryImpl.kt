package com.umc.halo.data.repository.themebox

import com.umc.halo.data.remote.api.themebox.ThemeBoxApi
import com.umc.halo.domain.model.themebox.ThemeBoxResult
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import javax.inject.Inject

class ThemeBoxRepositoryImpl @Inject constructor(
    val themeBoxApi: ThemeBoxApi
): ThemeBoxRepository {
    override suspend fun getThemeBox(): ThemeBoxResult {
        TODO("Not yet implemented")
    }
}