package com.umc.halo.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToHome = onNavigateToHome,
        modifier = modifier
    )
}

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState.currentStep) {
        OnboardingStep.NAME -> {
            // 이름 입력 화면
        }

        OnboardingStep.BASIC_INFO -> {
            // 성별 + 생년월일 화면
        }

        OnboardingStep.WELCOME -> {
            // 환영 화면
        }

        OnboardingStep.PARENT_PERSONALITY -> {
            // 부모님 성격 선택 화면
        }

        OnboardingStep.RELATIONSHIP -> {
            // 부모님과 관계 선택 화면
        }

        OnboardingStep.GOAL -> {
            // 되고 싶은 사이 선택 화면
        }

        OnboardingStep.COMPLETE -> {
            // 완료 화면
        }
    }
}