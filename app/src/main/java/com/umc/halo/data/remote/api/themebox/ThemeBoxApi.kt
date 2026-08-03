package com.umc.halo.data.remote.api.themebox

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.themebox.ThemeBoxResponse
import com.umc.halo.data.remote.dto.response.themebox.ThemeExhibitionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ThemeBoxApi {
    @GET("/api/v1/exhibitions")
    suspend fun getThemeBox(): BaseResponse<ThemeBoxResponse>

    @GET("/api/v1/exhibitions/{storybookId}/chapters")
    suspend fun getThemeExhibition(@Path("storybookId") storybookId: Long): BaseResponse<ThemeExhibitionResponse>
}