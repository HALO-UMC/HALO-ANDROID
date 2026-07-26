package com.umc.halo.presentation.login

import android.content.Context
import com.umc.halo.presentation.base.UiEvent

/**
 * 로그인 화면에서 사용자가 하는 행동
 * 기존 LoginScreen 의 onKakaoClick / onGoogleClick 콜백을 이벤트로 승격
 * sealed 로 ViewModel 의 when 에서 모든 케이스 처리
 */
sealed interface LoginUiEvent : UiEvent {
    // 카카오/구글 로그인 SDK 모두 계정 선택 UI 를 띄울 Activity Context 를 필요로 하여 이벤트에 담아 전달함
    data class KakaoLoginClicked(val context: Context) : LoginUiEvent
    data class GoogleLoginClicked(val context: Context) : LoginUiEvent

    // 약관동의로 이동을 처리한 뒤 1회성 신호를 내려 중복 이동 금지
    data object NavigationHandled : LoginUiEvent
}
