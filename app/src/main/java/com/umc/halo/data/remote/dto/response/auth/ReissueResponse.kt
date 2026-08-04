package com.umc.halo.data.remote.dto.response.auth

/**
 * POST /api/v1/auth/reissue 응답
 *
 * 서버가 refresh 토큰도 함께 회전(rotate)시키므로 두 토큰을 모두 새로 저장
 *
 * @param accessToken         새 Access Token
 * @param refreshToken        새 Refresh Token
 * @param onboardingCompleted 온보딩 완료 여부
 * @param termsAgreed         필수 약관 전체 동의 여부
 */
data class ReissueResponse(
    val accessToken: String,
    val refreshToken: String,
    val onboardingCompleted: Boolean?,
    val termsAgreed: Boolean?
)
