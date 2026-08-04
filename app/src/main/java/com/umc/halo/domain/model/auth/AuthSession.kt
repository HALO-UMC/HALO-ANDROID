package com.umc.halo.domain.model.auth

/**
 * 자동 로그인(토큰 재발급) 성공 결과
 *
 * @param termsAgreed         필수 약관 전체 동의 여부. 서버가 값을 주지 않으면 null
 * @param onboardingCompleted 온보딩 완료 여부. 서버가 값을 주지 않으면 null
 */
data class AuthSession(
    val termsAgreed: Boolean?,
    val onboardingCompleted: Boolean?
)
