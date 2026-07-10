package com.umc.halo.presentation.login

import com.umc.halo.presentation.base.BaseViewModel

/**
 * 로그인 화면의 상태/이벤트를 관리
 * DI가 추가되지 않아 @HiltViewModel / @Inject 없이 순수 클래스로 기본만 구현
 */
class LoginViewModel : BaseViewModel<LoginUiState, LoginUiEvent, Nothing>(
    initialState = LoginUiState()
) {
    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            LoginUiEvent.KakaoLoginClicked -> {
                // TODO: 카카오 SDK 로 providerToken 받기
            }

            LoginUiEvent.GoogleLoginClicked -> {
                // TODO: 구글 SDK 로 providerToken 받기
            }
        }
    }
}
