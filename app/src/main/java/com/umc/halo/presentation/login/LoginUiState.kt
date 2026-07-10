package com.umc.halo.presentation.login

/**
 * 로그인 화면의 상태
 * 실제 로그인 로직(소셜 SDK 연동)은 providerToken 종류 확정 및 서버 완성 후 추가 작업
 */
data class LoginUiState(
    val isLoading: Boolean = false
)
