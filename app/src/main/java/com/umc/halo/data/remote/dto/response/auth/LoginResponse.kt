package com.umc.halo.data.remote.dto.response.auth

/**
 * POST /api/v1/auth/login 응답
 * @param accessToken         서버 발급 Access Token
 * @param refreshToken        서버 발급 Refresh Token
 * @param isNewUser           이번 로그인으로 새로 가입된 회원인지
 * @param onboardingCompleted 온보딩 완료 여부
 */
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val onboardingCompleted: Boolean
)
