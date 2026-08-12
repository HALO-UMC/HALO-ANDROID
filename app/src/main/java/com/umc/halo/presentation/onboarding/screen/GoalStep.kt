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
import com.umc.halo.R
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
    onSystemBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onSystemBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(bottom = 110.dp)
        ) {
            OnboardingProgressBar(
                currentStep = 3,
                totalStep = 3,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 3.dp,
                    end = 20.dp
                )
            )

            Spacer(modifier = Modifier.height(31.dp))

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
                            id = R.drawable.ic_common_chevron_left
                        ),
                        contentDescription = "이전 화면",
                        tint = Gray800,
                        modifier = Modifier.size(8.dp, 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(19.dp))

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
                    style = HaloType.heading02Regular,
                    color = Gray800
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "해당하는 항목을 선택해주세요.",
                    style = HaloType.body03Regular,
                    color = Gray400
                )

                val limitMessage = uiState.goalLimitMessage
                val errorMessage = limitMessage ?: uiState.stepErrorMessage

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = errorMessage,
                        style = HaloType.body03Regular,
                        color = Error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                uiState.goalRelationshipTags.forEachIndexed { index, tag ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    val isSelected = tag in uiState.selectedGoals

                    GoalOptionCard(
                        text = tag.title,
                        selected = isSelected,
                        onClick = {
                            onEvent(
                                OnboardingUiEvent.GoalClicked(tag)
                            )
                        }
                    )
                }
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
            style = HaloType.body02Medium,
            color = textColor
        )
    }
}
