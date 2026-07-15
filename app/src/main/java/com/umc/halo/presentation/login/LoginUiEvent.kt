package com.umc.halo.presentation.login

import android.content.Context
import com.umc.halo.presentation.base.UiEvent

/**
 * 로그인 화면에서 사용자가 하는 행동
 * 기존 LoginScreen 의 onKakaoClick / onGoogleClick 콜백을 이벤트로 승격
 * sealed 로 ViewModel 의 when 에서 모든 케이스 처리
 */
sealed interface LoginUiEvent : UiEvent {
    // 카카오 로그인 SDK 는 Activity Context를 필요로 하여 이벤트에 담아 전달함
    data class KakaoLoginClicked(val context: Context) : LoginUiEvent
    data object GoogleLoginClicked : LoginUiEvent
}
