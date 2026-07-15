package com.umc.halo.domain.model.auth

/**
 * 로그인 성공 결과
 * 로그인 직후 화면 이동 분기(신규/온보딩 여부)에 필요한 값만 전달
 */
data class LoginResult(
    val isNewUser: Boolean,
    val onboardingCompleted: Boolean
)
