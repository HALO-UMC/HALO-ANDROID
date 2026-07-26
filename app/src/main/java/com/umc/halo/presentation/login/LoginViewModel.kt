package com.umc.halo.presentation.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.umc.halo.data.remote.auth.GoogleLoginDataSource
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
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val authRepository: AuthRepository
) : BaseViewModel<LoginUiState, LoginUiEvent>(
    initialState = LoginUiState()
) {
    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            // TODO: 아래 참고
            // [임시 테스트] 서버붙기 전 UI 흐름 확인용 — 실제 소셜 로그인 대신 바로 약관동의로 이동
            //   → 서버 연동 시: 아래 updateState 한 줄 삭제 + loginWithKakao(event.context) 주석 해제
            is LoginUiEvent.KakaoLoginClicked -> {
                updateState { copy(navigateToTerms = true) }
                // loginWithKakao(event.context)
            }
            is LoginUiEvent.GoogleLoginClicked -> {
                updateState { copy(navigateToTerms = true) }
                // loginWithGoogle(event.context)
            }
            // 약관동의로 이동을 처리한 뒤 중복 이동 방지
            LoginUiEvent.NavigationHandled -> updateState { copy(navigateToTerms = false) }
        }
    }

    private fun loginWithKakao(context: Context) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching {
                val idToken = kakaoLoginDataSource.login(context)            // 카카오에서 idToken
                authRepository.login(SocialProvider.KAKAO, idToken)          // 서버 로그인 → 토큰 저장 + 결과
            }.onSuccess { result ->
                // 로그인 성공 → 약관동의 화면으로 이동 (LoginUiState.navigateToTerms 신호)
                // TODO: result.isNewUser / result.onboardingCompleted 로 기존(온보딩 완료) 사용자는 홈으로 바로 보내는 분기 추가
                updateState { copy(navigateToTerms = true) }
            }.onFailure {
                // TODO: 로그인 실패 처리 (에러 UI/이펙트) — 현재는 isLoading 만 원복됨
            }
            updateState { copy(isLoading = false) }
        }
    }

    private fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching {
                val idToken = googleLoginDataSource.login(context)           // 구글에서 idToken
                authRepository.login(SocialProvider.GOOGLE, idToken)         // 서버 로그인 → 토큰 저장 + 결과
            }.onSuccess { result ->
                // 로그인 성공 → 약관동의 화면으로 이동 (LoginUiState.navigateToTerms 신호)
                // TODO: result.isNewUser / result.onboardingCompleted 로 기존(온보딩 완료) 사용자는 홈으로 바로 보내는 분기 추가
                updateState { copy(navigateToTerms = true) }
            }.onFailure {
                // TODO: 로그인 실패 처리 (에러 UI/이펙트) — 현재는 isLoading 만 원복됨
            }
            updateState { copy(isLoading = false) }
        }
    }
}
