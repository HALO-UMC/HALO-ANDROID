package com.umc.halo.presentation.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.umc.halo.data.auth.KakaoLoginDataSource
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면의 상태/이벤트를 관리
 * 카카오 로그인으로 OIDC idToken 을 받아옴 (서버 전송은 다음 단계에서 추가)
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource
    // TODO: 서버 완성 후 로그인 UseCase(=AuthRepository) 주입 → idToken 을 /auth/login 으로 전송
) : BaseViewModel<LoginUiState, LoginUiEvent>(
    initialState = LoginUiState()
) {
    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.KakaoLoginClicked -> loginWithKakao(event.context)

            LoginUiEvent.GoogleLoginClicked -> {
                // TODO: 구글 SDK 로 providerToken 받기
            }
        }
    }

    private fun loginWithKakao(context: Context) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching { kakaoLoginDataSource.login(context) }
                .onSuccess { idToken ->
                    // TODO: 이 idToken 을 providerToken 으로 서버 POST /api/v1/auth/login 전송
                }
                .onFailure {
                    // TODO: 로그인 실패 처리 (에러 UI/이펙트) — 현재는 isLoading 만 원복됨
                }
            updateState { copy(isLoading = false) }
        }
    }
}
