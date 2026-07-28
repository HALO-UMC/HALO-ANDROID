package com.umc.halo.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.presentation.theme.Primary500

/**
 * 스플래시 진입점
 *
 * 애니메이션이 도는 동안 자동 로그인을 시도하고 판정 결과에 따라 네 화면 중 하나로 보냄
 */
@Composable
fun SplashRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.destination) {
        val destination = uiState.destination ?: return@LaunchedEffect

        when (destination) {
            AuthDestination.LOGIN -> onNavigateToLogin()
            AuthDestination.TERMS -> onNavigateToTerms()
            AuthDestination.ONBOARDING -> onNavigateToOnboarding()
            AuthDestination.HOME -> onNavigateToHome()
        }

        viewModel.onEvent(SplashUiEvent.NavigationHandled)
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary500),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(600.dp)
        ) {
            DotLottieAnimation(
                source = DotLottieSource.Asset("splash.lottie"),
                autoplay = true,
                loop = false,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
