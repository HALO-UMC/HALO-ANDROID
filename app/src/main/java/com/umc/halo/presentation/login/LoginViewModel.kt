package com.umc.halo.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.umc.halo.core.datastore.DeviceUuidDataStore
import com.umc.halo.data.remote.auth.GoogleLoginDataSource
import com.umc.halo.data.remote.auth.KakaoLoginDataSource
import com.umc.halo.domain.model.auth.LoginCancelledException
import com.umc.halo.domain.model.auth.SocialProvider
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.notification.NotificationRepository
import com.umc.halo.domain.usecase.auth.ResolveDestinationAfterLoginUseCase
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면의 상태/이벤트를 관리
 *
 * 로그인 흐름: 소셜 SDK 로 OIDC idToken 획득 → 서버(/auth/login)에 전송 → 서버 토큰 저장
 *            → 약관/온보딩 상태를 확인해 다음 화면 결정
 * 카카오와 구글은 idToken 을 얻는 방법만 다르고 그 뒤는 같아서 login() 하나로 통일
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleLoginDataSource: GoogleLoginDataSource,
    private val authRepository: AuthRepository,
    private val resolveDestinationAfterLogin: ResolveDestinationAfterLoginUseCase,
    private val deviceUuidDataStore: DeviceUuidDataStore,
    private val notificationRepository: NotificationRepository
) : BaseViewModel<LoginUiState, LoginUiEvent>(
    initialState = LoginUiState()
) {
    init {
        // 재로그인 화면의 '최근 로그인' 배지에 쓸 값
        // 로컬에만 있는 정보임
        viewModelScope.launch {
            // updateState 의 람다는 suspend 가 아니라서 조회를 먼저 끝내고 상태에 넣는다
            val provider = runCatching { authRepository.getLastLoginProvider() }.getOrNull()
            updateState { copy(recentProvider = provider) }
        }
    }

    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.KakaoLoginClicked -> login(SocialProvider.KAKAO) {
                kakaoLoginDataSource.login(event.context)
            }

            is LoginUiEvent.GoogleLoginClicked -> login(SocialProvider.GOOGLE) {
                googleLoginDataSource.login(event.context)
            }

            // 이동/에러 표시를 처리한 뒤 신호를 내려 중복 실행 방지
            LoginUiEvent.NavigationHandled -> updateState { copy(destination = null) }
            LoginUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }
        }
    }

    /**
     * @param provider   서버에 보낼 소셜 제공자
     * @param getIdToken 해당 SDK 로 OIDC idToken 을 받아오는 방법
     */
    private fun login(provider: SocialProvider, getIdToken: suspend () -> String) {
        if (currentState.isLoading) return   // 로그인 진행 중 중복 탭 무시

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            runCatching {
                val idToken = getIdToken()

                val loginResult = authRepository.login(provider, idToken)

                resolveDestinationAfterLogin(loginResult)
            }.onSuccess { destination ->
                updateState { copy(destination = destination) }
                registerFCMToken()
            }.onFailure { throwable ->
                // 사용자가 직접 창을 닫은 건 실패가 아님으로 처리
                if (throwable !is LoginCancelledException) {
                    updateState { copy(errorMessage = LOGIN_FAILED_MESSAGE) }
                }
            }

            updateState { copy(isLoading = false) }
        }
    }

    /**
     * FCM 기기 등록 — 이 기기의 UUID 와 현재 푸시 토큰을 한 쌍으로 서버에 저장
     *
     * 로그인 본체와 달리 이 함수는 별도 코루틴이라 여기서 예외가 새면 로그인 성공 직후 앱이 종료되므로 반드시 가듐
     */
    private fun registerFCMToken() = viewModelScope.launch {
        runCatching {
            val uuid = deviceUuidDataStore.getOrCreate()
            val token = notificationRepository.getFcmToken()

            notificationRepository.addMembers(token, uuid)
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식)은 디자인 확정 후 교체
        const val LOGIN_FAILED_MESSAGE = "로그인에 실패했어요. 잠시 후 다시 시도해 주세요."
    }
}
