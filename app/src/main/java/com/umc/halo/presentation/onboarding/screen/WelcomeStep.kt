package com.umc.halo.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.umc.halo.presentation.onboarding.component.OnboardingBackButton
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary600
import kotlinx.coroutines.delay

@Composable
fun WelcomeStep(
    userName: String,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * 화면에 진입하면 다음 버튼을 바로 보여주지 않고,
     * 화면설계서에 따라 0.5초 후 표시.
     *
     * 화면을 벗어났다가 다시 진입하면 다시 0.5초의 지연이 적용됨.
     */
    var isNextButtonVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(500L)
        isNextButtonVisible = true
    }

    BackHandler(onBack = onBackClick)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        OnboardingBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 8.dp,
                    top = 14.dp
                )
        )

        val welcomeText = buildAnnotatedString {
            append("반가워요 ")

            withStyle(
                style = SpanStyle(
                    color = Primary600,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(userName)
            }

            append("님!")
        }

        Text(
            text = welcomeText,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(
                    top = 189.dp,
                    start = 21.dp,
                    end = 21.dp
                ),
            style = HaloType.heading01Regular.copy(textAlign = TextAlign.Center),
            color = Gray800
        )

        DotLottieAnimation(
            source = DotLottieSource.Asset(ONBOARDING_CHARACTER_LOTTIE),
            autoplay = true,
            loop = true,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 266.dp)
                .size(width = 152.dp, height = 177.dp)
        )

        Text(
            text = "좋은 관계는 작은 관심에서 시작됩니다!",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(
                    top = 454.dp,
                    start = 21.dp,
                    end = 21.dp
                ),
            style = HaloType.body01Regular.copy(textAlign = TextAlign.Center),
            color = Gray800
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 20.dp
                )
        ) {
            AnimatedVisibility(
                visible = isNextButtonVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 200
                    )
                )
            ) {
                OnboardingBottomButton(
                    text = "다음",
                    enabled = true,
                    onClick = onNextClick
                )
            }
        }
    }
}

private const val ONBOARDING_CHARACTER_LOTTIE = "onboarding_charactermotion1.lottie"
