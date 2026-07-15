package com.umc.halo.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

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
    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep == OnboardingStep.COMPLETE) {
            // 완료 화면 자체도 UI로 보여줄 예정이라 지금은 이동하지 않음
        }
    }

    when (uiState.currentStep) {
        OnboardingStep.NAME -> {
            NameInputStep(
                uiState = uiState,
                onEvent = onEvent,
                modifier = modifier
            )
        }

        OnboardingStep.BASIC_INFO -> {
            TemporaryStep(
                text = "성별 + 생년월일 화면",
                onNextClick = { onEvent(OnboardingUiEvent.NextClicked) },
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.WELCOME -> {
            TemporaryStep(
                text = "환영 화면\n${uiState.userName}님!",
                onNextClick = { onEvent(OnboardingUiEvent.NextClicked) },
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.PARENT_PERSONALITY -> {
            TemporaryStep(
                text = "부모님 성격 선택 화면",
                onNextClick = { onEvent(OnboardingUiEvent.NextClicked) },
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.RELATIONSHIP -> {
            TemporaryStep(
                text = "부모님과 나의 관계 선택 화면",
                onNextClick = { onEvent(OnboardingUiEvent.NextClicked) },
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.GOAL -> {
            TemporaryStep(
                text = "원하는 관계 선택 화면",
                onNextClick = { onEvent(OnboardingUiEvent.NextClicked) },
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.COMPLETE -> {
            TemporaryStep(
                text = "온보딩 완료 화면",
                onNextClick = onNavigateToHome,
                onBackClick = { onEvent(OnboardingUiEvent.BackClicked) },
                enabled = true,
                buttonText = "시작하기",
                modifier = modifier
            )
        }
    }
}

@Composable
private fun NameInputStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(140.dp))

        Text(
            text = "이름을 알려주세요",
            style = HaloType.heading01SemiBold,
            color = Gray800
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "HALO가 당신을 어떻게 부르면 좋을까요?",
            style = HaloType.body02Medium,
            color = Gray400
        )

        Spacer(modifier = Modifier.height(52.dp))

        OnboardingNameTextField(
            value = uiState.name,
            onValueChange = { name ->
                onEvent(OnboardingUiEvent.NameChanged(name))
            },
            placeholder = "이름 입력"
        )

        val nameErrorMessage = uiState.nameErrorMessage

        if (nameErrorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nameErrorMessage,
                style = HaloType.body03Medium,
                color = Primary500,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "2~10자, 한글/영어/숫자만 입력할 수 있어요.",
                style = HaloType.body03Medium,
                color = Gray400,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = {
                onEvent(OnboardingUiEvent.NextClicked)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun OnboardingNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(
            width = 1.dp,
            color = if (value.isNotBlank()) Primary500 else Gray100
        )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = HaloType.body01Medium.copy(
                color = Gray800
            ),
            cursorBrush = SolidColor(Primary500),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 17.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            style = HaloType.body01Medium,
                            color = Gray300
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun TemporaryStep(
    text: String,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buttonText: String = "다음"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = HaloType.heading02SemiBold,
            color = Gray800
        )

        Spacer(modifier = Modifier.height(40.dp))

        OnboardingBottomButton(
            text = buttonText,
            enabled = enabled,
            onClick = onNextClick
        )
    }
}