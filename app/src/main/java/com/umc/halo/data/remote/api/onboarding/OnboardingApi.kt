package com.umc.halo.data.remote.api.onboarding

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.onboarding.OnboardingSaveRequest
import com.umc.halo.data.remote.dto.response.onboarding.NicknameCheckResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingSaveResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingStatusResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingTagsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OnboardingApi {

    @GET("api/v1/onboarding/tags")
    suspend fun getTags(): BaseResponse<OnboardingTagsResponse>

    @GET("api/v1/onboarding/nickname/check")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): BaseResponse<NicknameCheckResponse>

    @GET("api/v1/onboarding/status")
    suspend fun getStatus(): BaseResponse<OnboardingStatusResponse>

    @POST("api/v1/onboarding")
    suspend fun saveOnboarding(
        @Body request: OnboardingSaveRequest
    ): BaseResponse<OnboardingSaveResponse>
}
