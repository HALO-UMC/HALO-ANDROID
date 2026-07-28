package com.umc.halo.data.remote.api.auth

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.auth.LoginRequest
import com.umc.halo.data.remote.dto.request.auth.ReissueRequest
import com.umc.halo.data.remote.dto.response.auth.LoginResponse
import com.umc.halo.data.remote.dto.response.auth.ReissueResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 인증관련 서버 API
 *
 */
interface AuthApi {

    // 소셜 로그인: providerToken(OIDC idToken) 검증 → 신규면 가입 → 서버 토큰 발급
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    // 토큰 재발급: 자동 로그인(스플래시)과 401 응답 시 TokenAuthenticator 가 사용
    @POST("api/v1/auth/reissue")
    suspend fun reissue(@Body request: ReissueRequest): BaseResponse<ReissueResponse>

    // 로그아웃: 서버가 accessToken 의 memberId 로 refreshToken 을 찾아 무효화 (result 는 항상 null)
    @POST("api/v1/auth/logout")
    suspend fun logout(): BaseResponse<Unit>
}
