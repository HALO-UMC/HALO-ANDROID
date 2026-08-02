package com.umc.halo.data.remote.api.storybook

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.storybook.StorybookDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StorybookDetailApi {
    @GET("/api/v1/storybooks/{storybookId}")
    suspend fun getStorybookDetail(@Path("storybookId") storybookId: Long): BaseResponse<StorybookDetailResponse>
}