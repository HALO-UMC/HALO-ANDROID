package com.umc.halo.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.onboarding.GOAL_OPTIONS
import com.umc.halo.presentation.onboarding.OnboardingUiEvent
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.onboarding.component.OnboardingProgressBar
import com.umc.halo.presentation.theme.Error
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary600

@Composable
fun GoalStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * 기기 시스템 뒤로가기 버튼을 눌렀을 때도
     * 현재 관계 선택 단계로 이동한다.
     */
    BackHandler {
        onEvent(OnboardingUiEvent.BackClicked)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 360.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(bottom = 110.dp)
        ) {
            /*
             * 세 번째 온보딩 단계이므로
             * 진행 바의 세 번째 칸이 진하게 표시된다.
             */
            OnboardingProgressBar(
                currentStep = 3,
                totalStep = 3,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 14.dp,
                    end = 20.dp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            /*
             * 이전 화면으로 이동하는 버튼
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                IconButton(
                    onClick = {
                        onEvent(OnboardingUiEvent.BackClicked)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_chevron_left
                        ),
                        contentDescription = "이전 화면",
                        tint = Gray800,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                val titleText = buildAnnotatedString {
                    append("${uiState.userName}님은 부모님과\n")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("어떤 사이")
                    }

                    append("가 되고 싶나요?")
                }

                Text(
                    text = titleText,
                    style = HaloType.heading01Regular.copy(
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    color = Gray800
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "해당되는 항목을 모두 선택해주세요. (최대 2개).",
                    style = HaloType.body03Regular.copy(
                        fontSize = 12.sp,
                        lineHeight = 17.4.sp,
                        letterSpacing = (-0.12).sp
                    ),
                    color = Gray400
                )

                val limitMessage = uiState.goalLimitMessage

                if (limitMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = limitMessage,
                        style = HaloType.body03Regular.copy(
                            fontSize = 12.sp,
                            lineHeight = 17.4.sp,
                            letterSpacing = (-0.12).sp
                        ),
                        color = Error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                GOAL_OPTIONS.forEachIndexed { index, goal ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    val isSelected = goal in uiState.selectedGoals

                    GoalOptionCard(
                        text = goal,
                        selected = isSelected,
                        onClick = {
                            onEvent(
                                OnboardingUiEvent.GoalClicked(goal)
                            )
                        }
                    )
                }
            }
        }

        /*
         * 한 개 이상 선택한 경우 다음 버튼 활성화
         */
        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = {
                onEvent(OnboardingUiEvent.NextClicked)
            },
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
private fun GoalOptionCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        Primary50
    } else {
        Gray30
    }

    val textColor = if (selected) {
        Primary600
    } else {
        Gray700
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
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
            color = textColor
        )
    }
}