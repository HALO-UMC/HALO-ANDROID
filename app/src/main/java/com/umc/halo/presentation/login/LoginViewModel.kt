package com.umc.halo.presentation.login

import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 로그인 화면의 상태/이벤트를 관리
 * 현재 주입할 의존성이 없어 생성자가 비어 있으나 서버/SDK 완성 후 로그인 UseCase 를 이 생성자에 추가할 예정
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    // TODO: 로그인 UseCase 주입 예정 (providerToken 확정 + 서버 완성 후)
) : BaseViewModel<LoginUiState, LoginUiEvent>(
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
