package com.umc.halo.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBackButton
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun CompleteStep(
    uiState: OnboardingUiState,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * 현재 부모님과의 관계 1개와
     * 목표 관계 1~2개를 합쳐 완료 화면에 표시한다.
     */
    val selectedDirections = buildList {
        uiState.selectedRelationship?.let { relationship ->
            add(relationship)
        }

        addAll(uiState.selectedGoals)
    }

    BackHandler(onBack = onBackClick)

    BoxWithConstraints(
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

        /*
         * 화면 높이에 비례해 제목 위치를 고정한다.
         *
         * 카드 개수가 바뀌어도 제목 위치는 움직이지 않는다.
         */
        val titleTopPosition = maxHeight * 0.23f

        /*
         * 선택 목록은 제목 영역과 별도로 배치한다.
         *
         * 이 값을 크게 하면
         * "선택한 관계 방향" 영역이 더 아래로 내려간다.
         */
        val selectedSectionTopPosition =
            titleTopPosition + 257.dp

        DotLottieAnimation(
            source = DotLottieSource.Asset(ONBOARDING_CHARACTER_LOTTIE),
            autoplay = true,
            loop = true,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = titleTopPosition + 63.dp)
                .size(width = 130.dp, height = 151.dp)
        )

        /*
         * 완료 안내 문구
         */
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = titleTopPosition),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Medium)
                    ) {
                        append("모든 준비")
                    }

                    append("가 끝났어요!")
                },
                style = HaloType.heading01Regular,
                color = Gray800,
                textAlign = TextAlign.Center
            )

        }

        /*
         * 선택한 관계 방향
         *
         * 제목 영역과 별도로 고정되어 있기 때문에
         * 카드가 2개 또는 3개여도 제목 위치는 그대로다.
         */
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = selectedSectionTopPosition)
        ) {
            Text(
                text = "선택한 관계 방향",
                style = HaloType.body02SemiBold,
                color = Gray500
            )

            Spacer(modifier = Modifier.height(16.dp))

            selectedDirections.forEachIndexed { index, direction ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(11.dp))
                }

                SelectedDirectionCard(
                    text = direction.withSentencePeriod()
                )
            }
        }

        /*
         * 시작하기 버튼은 항상 화면 하단에 고정한다.
         */
        OnboardingBottomButton(
            text = "시작하기",
            enabled = true,
            onClick = onStartClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
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
private fun SelectedDirectionCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Gray30)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium,
            color = Gray700
        )
    }
}

private fun String.withSentencePeriod(): String {
    return if (
        endsWith(".") ||
        endsWith("!") ||
        endsWith("?")
    ) {
        this
    } else {
        "$this."
    }
}

private const val ONBOARDING_CHARACTER_LOTTIE = "onboarding_charactermotion1.lottie"
