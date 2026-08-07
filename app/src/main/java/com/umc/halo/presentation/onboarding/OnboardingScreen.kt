package com.umc.halo.presentation.onboarding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.R
import com.umc.halo.presentation.mypage.component.ConfirmActionDialog
import com.umc.halo.presentation.onboarding.component.OnboardingBackButton
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.onboarding.screen.BasicInfoStep
import com.umc.halo.presentation.onboarding.screen.GoalStep
import com.umc.halo.presentation.onboarding.screen.ParentPersonalityStep
import com.umc.halo.presentation.onboarding.screen.RelationshipStep
import com.umc.halo.presentation.onboarding.screen.WelcomeStep
import com.umc.halo.presentation.onboarding.screen.CompleteStep
import com.umc.halo.presentation.theme.Error
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Success
import com.umc.halo.presentation.theme.White

@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    //토큰 등록
    viewModel.registerFCMToken()

    //알림 권한 허용 되었는지 판단
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            //모든 알림 끄기
            viewModel.offAllNotification()
        }
    }

    LaunchedEffect(Unit) {
        if ( // 권한 허용이 되지 않았을 시
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //알림 권한 허용 팝업 띄우기
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            onNavigateToHome()
            viewModel.onEvent(OnboardingUiEvent.HomeNavigationHandled)
        }
    }

    OnboardingScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToHome = onNavigateToHome,
        modifier = modifier
    )
}

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    var showExitDialog by remember { mutableStateOf(false) }
    val onSystemBack = {
        showExitDialog = true
    }
    val onBackClick = {
        if (uiState.currentStep == OnboardingStep.NAME) {
            onNavigateBack()
        } else {
            onEvent(OnboardingUiEvent.BackClicked)
        }
    }

    when (uiState.currentStep) {
        OnboardingStep.NAME -> {
            NameInputStep(
                uiState = uiState,
                onEvent = onEvent,
                onBackClick = onBackClick,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.BASIC_INFO -> {
            BasicInfoStep(
                uiState = uiState,
                onEvent = onEvent,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.WELCOME -> {
            WelcomeStep(
                userName = uiState.userName,
                onBackClick = onBackClick,
                onNextClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.PARENT_PERSONALITY -> {
            ParentPersonalityStep(
                uiState = uiState,
                onEvent = onEvent,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.RELATIONSHIP -> {
            RelationshipStep(
                uiState = uiState,
                onEvent = onEvent,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.GOAL -> {
            GoalStep(
                uiState = uiState,
                onEvent = onEvent,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }

        OnboardingStep.COMPLETE -> {
            CompleteStep(
                uiState = uiState,
                onStartClick = onNavigateToHome,
                onSystemBack = onSystemBack,
                modifier = modifier
            )
        }
    }

    if (showExitDialog) {
        ConfirmActionDialog(
            title = "앱을 종료하시겠습니까?",
            description = "확인을 누르면 HALO가 종료됩니다.",
            buttonText = "종료",
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                activity?.finish()
            }
        )
    }
}

@Composable
private fun NameInputStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    onBackClick: () -> Unit,
    onSystemBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nameErrorMessage = uiState.nameErrorMessage
    val isNameError = nameErrorMessage != null

    BackHandler(onBack = onSystemBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OnboardingBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(
                    start = 8.dp,
                    top = 14.dp
                )
        )

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
                onClearClick = {
                    onEvent(OnboardingUiEvent.NameChanged(""))
                },
                placeholder = "이름을 입력하세요.",
                isError = isNameError,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            if (nameErrorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))

                NameErrorGuide(
                    message = nameErrorMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(10.dp))

                NameRequirementGuide(
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 44.dp)
                )
            }
        }

        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = {
                onEvent(OnboardingUiEvent.NextClicked)
            },
            clickableWhenDisabled = true,
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
    onClearClick: () -> Unit,
    placeholder: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
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
            textStyle = HaloType.body02Medium.copy(
                color = Gray800
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
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 24.dp,
                                end = if (value.isNotEmpty()) 50.dp else 24.dp
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                style = HaloType.body02Regular,
                                color = Gray300
                            )
                        }

                        innerTextField()
                    }

                    if (value.isNotEmpty()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                                .size(18.dp)
                                .clickable(onClick = onClearClick),
                            shape = CircleShape,
                            color = Gray200
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_common_close),
                                    contentDescription = "입력 내용 지우기",
                                    tint = White,
                                    modifier = Modifier.size(9.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun NameRequirementGuide(
    uiState: OnboardingUiState,
    modifier: Modifier = Modifier
) {
    val hasInput = uiState.name.isNotEmpty()

    Column(modifier = modifier) {
        NameRequirementRow(
            text = "최소 2자 ~ 최대 10자 이내",
            isSatisfied = uiState.isNameLengthValid,
            hasInput = hasInput
        )
        NameRequirementRow(
            text = "한글/영어/숫자 조합 가능",
            isSatisfied = uiState.isNameFormatValid,
            hasInput = hasInput
        )
        NameRequirementRow(
            text = "특수 문자 입력 불가능",
            isSatisfied = uiState.isNameFormatValid,
            hasInput = hasInput
        )
    }
}

@Composable
private fun NameRequirementRow(
    text: String,
    isSatisfied: Boolean,
    hasInput: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = when {
        !hasInput -> Gray300
        isSatisfied -> Success
        else -> Error
    }

    Row(
        modifier = modifier.height(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_terms_check),
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = text,
            style = HaloType.caption01Medium,
            color = textColor
        )
    }
}

@Composable
private fun NameErrorGuide(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ⓘ",
            modifier = Modifier.width(16.dp),
            style = HaloType.caption01Medium,
            color = Error
        )
        Text(
            text = message,
            style = HaloType.caption01Medium,
            color = Error
        )
    }
}

@Composable
private fun TemporaryStep(
    text: String,
    onNextClick: () -> Unit,
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
