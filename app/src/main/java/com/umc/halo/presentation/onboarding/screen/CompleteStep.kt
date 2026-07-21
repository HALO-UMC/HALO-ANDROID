package com.umc.halo.presentation.onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Black
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun CompleteStep(
    uiState: OnboardingUiState,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * 현재 관계 1개와 원하는 관계 방향 1~2개를 합친다.
     *
     * 예시:
     * 대체로 좋은 편이에요.
     * 같이 보내는 시간을 만들고 싶어요.
     * 마음을 표현해보고 싶어요.
     */
    val selectedDirections = buildList {
        uiState.selectedRelationship?.let { relationship ->
            add(relationship.withFinalPeriod())
        }

        addAll(
            uiState.selectedGoals.map { goal ->
                goal.withFinalPeriod()
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /*
         * 화면 중앙을 기준으로 배치하므로
         * 특정 화면 좌표에 고정하는 방식보다
         * 여러 화면 크기에서 안정적으로 표시된다.
         */
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 360.dp)
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                )
        ) {
            Text(
                text = "모든 준비가 끝났어요!",
                modifier = Modifier.fillMaxWidth(),
                style = HaloType.heading01Regular.copy(
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.48).sp
                ),
                color = Gray800,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "이제 HALO 와 함께 부모님과의 관계를\n한 장씩 기록해볼까요?",
                modifier = Modifier.fillMaxWidth(),
                style = HaloType.body01Regular.copy(
                    fontSize = 16.sp,
                    lineHeight = 23.2.sp,
                    letterSpacing = (-0.16).sp
                ),
                color = Gray800,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "선택한 관계 방향",
                style = HaloType.body01SemiBold.copy(
                    fontSize = 16.sp,
                    lineHeight = 23.2.sp,
                    letterSpacing = (-0.16).sp
                ),
                color = Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            selectedDirections.forEachIndexed { index, direction ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                SelectedDirectionCard(
                    text = direction
                )
            }
        }

        OnboardingBottomButton(
            text = "시작하기",
            enabled = true,
            onClick = onStartClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = 360.dp)
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
            style = HaloType.body02Medium.copy(
                fontSize = 14.sp,
                lineHeight = 20.3.sp,
                letterSpacing = (-0.14).sp
            ),
            color = Gray700,
            maxLines = 1
        )
    }
}

/*
 * 선택지 문자열에 마침표가 없다면 완료 화면에서만 붙여준다.
 * 원본 상태값은 변경하지 않는다.
 */
private fun String.withFinalPeriod(): String {
    return if (endsWith(".")) {
        this
    } else {
        "$this."
    }
}