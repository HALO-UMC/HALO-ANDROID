package com.umc.halo.presentation.splash

import com.umc.halo.domain.model.auth.AuthDestination

/**
 * 스플래시 화면 상태
 *
 * @param destination 판정이 끝난 뒤 이동할 화면. 아직 판정 중이면 null
 *                    화면은 이 값이 채워지는 순간 이동
 */
data class SplashUiState(
    val destination: AuthDestination? = null
)
