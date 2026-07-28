package com.umc.halo.presentation.login

import com.umc.halo.domain.model.auth.AuthDestination

/**
 * 로그인 화면의 상태
 *
 * @param isLoading    소셜 SDK 호출 ~ 서버 로그인이 끝날 때까지 true. 버튼 중복 탭을 막는 데 사용
 * @param destination  로그인 성공 후 이동할 화면(약관/온보딩/홈). 이동 전까지만 채워지는 1회성 신호
 * @param errorMessage 로그인 실패 안내 문구. 사용자가 직접 취소한 경우에는 채우지 않음
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val destination: AuthDestination? = null,
    val errorMessage: String? = null
)
