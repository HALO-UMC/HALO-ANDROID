package com.umc.halo.presentation.onboarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.onboarding.MAX_PARENT_PERSONALITY_COUNT
import com.umc.halo.presentation.onboarding.OnboardingUiEvent
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.PARENT_PERSONALITY_GROUPS
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.onboarding.component.OnboardingChoiceChip
import com.umc.halo.presentation.onboarding.component.OnboardingProgressBar
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParentPersonalityStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 360.dp)
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            /*
             * 진행 바는 스크롤 영역과 분리하여
             * 화면 상단에 고정한다.
             */
            OnboardingProgressBar(
                currentStep = 1,
                totalStep = 3,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 14.dp,
                    end = 20.dp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            /*
             * 뒤로 가기 영역도 진행 바 아래에 고정한다.
             * IconButton의 터치 영역은 44dp로 유지한다.
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

            /*
             * 남은 화면 높이를 선택 영역이 차지한다.
             * 화면 높이가 작거나 내용이 길어질 경우
             * 이 영역만 세로로 스크롤된다.
             */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 8.dp,
                        bottom = 24.dp
                    )
            ) {
                val titleText = buildAnnotatedString {
                    append("${uiState.userName}님이 생각하시는\n")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("부모님의 성격")
                    }

                    append("을 선택해주세요.")
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
                    text = "해당되는 항목을 모두 선택해주세요. (최대 3개).",
                    style = HaloType.body03Regular.copy(
                        fontSize = 12.sp,
                        lineHeight = 17.4.sp,
                        letterSpacing = (-0.12).sp
                    ),
                    color = Gray400
                )

                Spacer(modifier = Modifier.height(24.dp))

                PARENT_PERSONALITY_GROUPS.forEachIndexed { index, group ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = group.title,
                        style = HaloType.body02Medium.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.3.sp,
                            letterSpacing = (-0.14).sp
                        ),
                        color = Gray700
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        group.options.forEach { personality ->
                            val isSelected =
                                personality in uiState.selectedParentPersonalities

                            /*
                             * 전체 선택 개수가 3개 미만이거나,
                             * 이미 선택된 태그인 경우에만 누를 수 있다.
                             *
                             * 선택된 태그는 3개가 선택된 상태에서도
                             * 다시 눌러 선택을 해제할 수 있어야 한다.
                             */
                            val isEnabled =
                                isSelected ||
                                        uiState.selectedParentPersonalities.size <
                                        MAX_PARENT_PERSONALITY_COUNT

                            OnboardingChoiceChip(
                                text = personality,
                                selected = isSelected,
                                enabled = isEnabled,
                                onClick = {
                                    onEvent(
                                        OnboardingUiEvent.ParentPersonalityClicked(
                                            personality
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            /*
             * 하단 버튼은 스크롤 영역 밖에 배치하여
             * 화면 크기와 관계없이 항상 하단에 유지한다.
             */
            OnboardingBottomButton(
                text = "다음",
                enabled = uiState.isNextEnabled,
                onClick = {
                    onEvent(OnboardingUiEvent.NextClicked)
                },
                modifier = Modifier
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
}