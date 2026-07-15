package com.umc.halo.data.remote.dto.request.auth

/**
 * POST /api/v1/auth/login 요청 body
 *
 * @param provider      소셜 제공자 문자열 (KAKAO / GOOGLE)
 * @param providerToken SDK 가 발급한 OIDC ID Token (카카오 idToken)
 */
data class LoginRequest(
    val provider: String,
    val providerToken: String
)
