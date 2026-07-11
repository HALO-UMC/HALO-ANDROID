package com.umc.halo.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState.currentStep) {
            OnboardingStep.NAME -> {
                Text(text = "이름 입력 화면")
            }

            OnboardingStep.BASIC_INFO -> {
                Text(text = "성별 + 생년월일 화면")
            }

            OnboardingStep.WELCOME -> {
                Text(text = "환영 화면")
            }

            OnboardingStep.PARENT_PERSONALITY -> {
                Text(text = "부모님 성격 선택 화면")
            }

            OnboardingStep.RELATIONSHIP -> {
                Text(text = "부모님과 관계 선택 화면")
            }

            OnboardingStep.GOAL -> {
                Text(text = "되고 싶은 사이 선택 화면")
            }

            OnboardingStep.COMPLETE -> {
                Text(text = "온보딩 완료 화면")
            }
        }
    }
}