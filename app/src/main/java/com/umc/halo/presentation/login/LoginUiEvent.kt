package com.umc.halo.presentation.login

import com.umc.halo.presentation.base.UiEvent

/**
 * 로그인 화면에서 사용자가 하는 행동
 * 기존 LoginScreen 의 onKakaoClick / onGoogleClick 콜백을 이벤트로 승격
 * sealed 로 ViewModel 의 when 에서 모든 케이스 처리
 */
sealed interface LoginUiEvent : UiEvent {
    data object KakaoLoginClicked : LoginUiEvent
    data object GoogleLoginClicked : LoginUiEvent
}
