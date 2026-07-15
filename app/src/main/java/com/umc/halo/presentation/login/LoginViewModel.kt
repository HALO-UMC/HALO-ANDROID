package com.umc.halo.presentation.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.umc.halo.data.remote.auth.KakaoLoginDataSource
import com.umc.halo.domain.model.auth.SocialProvider
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면의 상태/이벤트를 관리
 * 카카오 로그인 흐름: 카카오 SDK 로 OIDC idToken 획득 → 서버(/auth/login)에 전송 → 서버 토큰 저장 + 결과(신규/온보딩 여부) 반환
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val authRepository: AuthRepository
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
            runCatching {
                val idToken = kakaoLoginDataSource.login(context)            // 카카오에서 idToken
                authRepository.login(SocialProvider.KAKAO, idToken)          // 서버 로그인 → 토큰 저장 + 결과
            }.onSuccess { result ->
                // TODO: result.isNewUser / result.onboardingCompleted 로 화면 이동 분기 (온보딩 or 홈)
            }.onFailure {
                // TODO: 로그인 실패 처리 (에러 UI/이펙트) — 현재는 isLoading 만 원복됨
            }
            updateState { copy(isLoading = false) }
        }
    }
}
