package com.umc.halo.data.remote.api.themebox

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.themebox.ThemeBoxResponse
import retrofit2.http.GET

interface ThemeBoxApi {
    @GET("/api/v1/exhibitions")
    suspend fun getThemeBox(): BaseResponse<ThemeBoxResponse>
}