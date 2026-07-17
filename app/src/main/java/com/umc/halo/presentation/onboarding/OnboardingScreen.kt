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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Error
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500

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
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.WELCOME -> {
            TemporaryStep(
                text = "환영 화면\n${uiState.userName}님!",
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.PARENT_PERSONALITY -> {
            TemporaryStep(
                text = "부모님 성격 선택 화면",
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.RELATIONSHIP -> {
            TemporaryStep(
                text = "부모님과 나의 관계 선택 화면",
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.GOAL -> {
            TemporaryStep(
                text = "원하는 관계 선택 화면",
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
                enabled = uiState.isNextEnabled,
                modifier = modifier
            )
        }

        OnboardingStep.COMPLETE -> {
            TemporaryStep(
                text = "온보딩 완료 화면",
                onNextClick = onNavigateToHome,
                onBackClick = {
                    onEvent(OnboardingUiEvent.BackClicked)
                },
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
    val nameErrorMessage = uiState.nameErrorMessage
    val isNameError = nameErrorMessage != null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 153.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val titleText = buildAnnotatedString {
                append("안녕하세요!\n")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("당신의 이름")
                }

                append("을 알려주세요")
            }

            Text(
                text = titleText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 21.dp),
                style = HaloType.heading01Regular.copy(
                    textAlign = TextAlign.Center
                ),
                color = Gray800
            )

            Spacer(modifier = Modifier.height(35.dp))

            OnboardingNameTextField(
                value = uiState.name,
                onValueChange = { name ->
                    onEvent(OnboardingUiEvent.NameChanged(name))
                },
                placeholder = "이름을 입력하세요.",
                isError = isNameError,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            if (nameErrorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = nameErrorMessage,
                    style = HaloType.body03Regular,
                    color = Error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }

        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = {
                onEvent(OnboardingUiEvent.NextClicked)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 20.dp
                )
        )
    }
}

@Composable
private fun OnboardingNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        color = Gray30,
        border = BorderStroke(
            width = 1.dp,
            color = if (isError) {
                Error
            } else {
                Gray50
            }
        )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = HaloType.body02Regular.copy(
                color = Gray800,
                lineHeight = 20.3.sp,
                letterSpacing = (-0.14).sp
            ),
            cursorBrush = SolidColor(Primary500),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxSize(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            style = HaloType.body02Regular.copy(
                                lineHeight = 20.3.sp,
                                letterSpacing = (-0.14).sp
                            ),
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