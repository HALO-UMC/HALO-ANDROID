package com.umc.halo.presentation.splash

import com.umc.halo.presentation.base.UiEvent

/**
 * 스플래시 화면 이벤트

 */
sealed interface SplashUiEvent : UiEvent {

    // 화면 이동을 처리한 뒤 1회성 신호를 내려 중복 이동 방지
    data object NavigationHandled : SplashUiEvent
}
