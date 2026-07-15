package com.umc.halo.data.remote.api.auth

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.auth.LoginRequest
import com.umc.halo.data.remote.dto.response.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 인증관련 서버 API
 */
interface AuthApi {

    // 소셜 로그인: providerToken(OIDC idToken) 검증 → 신규면 가입 → 서버 토큰 발급
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>
}
