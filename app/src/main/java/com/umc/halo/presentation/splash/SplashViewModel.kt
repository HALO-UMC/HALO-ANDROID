package com.umc.halo.presentation.splash

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.domain.usecase.auth.ResolveStartDestinationUseCase
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 스플래시 화면 ViewModel
 *
 * 자동 로그인을 시도해 첫 화면(로그인/약관/온보딩/홈)을 결정
 * 판정 규칙 자체는 ResolveStartDestinationUseCase 에 있음
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val resolveStartDestination: ResolveStartDestinationUseCase
) : BaseViewModel<SplashUiState, SplashUiEvent>(SplashUiState()) {

    init {
        viewModelScope.launch {
            // 서버 조회를 먼저 '시작'만 해두고(async) 그 사이에 애니메이션 처리
            // 이렇게 하면 애니메이션 처리(5초) 와 API 처리시간 중 더 오래 걸리는 쪽만 기다림
            val destination = async {
                // 네트워크 오류 등으로 판정 자체가 실패하면 로그인 화면으로 이동
                runCatching { resolveStartDestination() }
                    .getOrDefault(AuthDestination.LOGIN)
            }

            delay(SPLASH_MINIMUM_DISPLAY_MILLIS)

            val resolved = destination.await()
            updateState { copy(destination = resolved) }
        }
    }

    override fun onEvent(event: SplashUiEvent) {
        when (event) {
            SplashUiEvent.NavigationHandled -> updateState { copy(destination = null) }
        }
    }

    private companion object {
        // 스플래시 애니메이션(splash.lottie) 재생 시간
        const val SPLASH_MINIMUM_DISPLAY_MILLIS = 5_000L
    }
}
