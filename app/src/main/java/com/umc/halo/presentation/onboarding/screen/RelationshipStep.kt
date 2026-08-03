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
import com.umc.halo.R
import com.umc.halo.presentation.onboarding.OnboardingUiEvent
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.onboarding.component.OnboardingProgressBar
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary600

private data class RelationshipOption(
    val title: String,
    val description: String
)

private val relationshipOptions = listOf(
    RelationshipOption(
        title = "매우 가까운 편이에요",
        description = "부모이기 전에 친구 같은, 비밀까지 나누는 사이"
    ),
    RelationshipOption(
        title = "대체로 좋은 편이에요",
        description = "일상적인 안부를 나누며 서로를 존중해요"
    ),
    RelationshipOption(
        title = "보통이에요",
        description = "가끔 어색하지만 필요한 이야기는 나눠요"
    ),
    RelationshipOption(
        title = "서먹한 편이에요",
        description = "대화가 많지 않고 거리감이 느껴질 때가 있어요"
    ),
    RelationshipOption(
        title = "멀어진 것 같아요",
        description = "최근에 갈등이 있거나 거의 연락하지 않아요"
    )
)

@Composable
fun RelationshipStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    /*
     * 기기 시스템 뒤로가기 버튼을 눌렀을 때도
     * 부모님 성격 선택 단계로 이동한다.
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
             * 온보딩 세 단계 중 두 번째 단계
             */
            OnboardingProgressBar(
                currentStep = 2,
                totalStep = 3,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 3.dp,
                    end = 20.dp
                )
            )

            Spacer(modifier = Modifier.height(47.dp))

            /*
             * 이전 버튼 영역
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

            Spacer(modifier = Modifier.height(19.dp))

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                val titleText = buildAnnotatedString {
                    append("${uiState.userName}님이 생각하시는\n")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("부모님과 나의 관계")
                    }

                    append("를 정의해주세요!")
                }

                Text(
                    text = titleText,
                    style = HaloType.heading02Regular,
                    color = Gray800
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "지금 우리의 관계와 가장 가까운 것을 골라주세요.",
                    style = HaloType.body03Regular,
                    color = Gray400
                )

                Spacer(modifier = Modifier.height(24.dp))

                relationshipOptions.forEachIndexed { index, option ->
                    val isSelected =
                        uiState.selectedRelationship == option.title

                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    RelationshipOptionCard(
                        option = option,
                        selected = isSelected,
                        onClick = {
                            onEvent(
                                OnboardingUiEvent.RelationshipClicked(
                                    option.title
                                )
                            )
                        }
                    )
                }
            }
        }

        /*
         * 관계를 하나 선택하면 활성화된다.
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
private fun RelationshipOptionCard(
    option: RelationshipOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        Primary50
    } else {
        Gray30
    }

    val titleColor = if (selected) {
        Primary600
    } else {
        Gray700
    }

    val descriptionColor = if (selected) {
        Primary400
    } else {
        Gray500
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(63.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = option.title,
            style = HaloType.body02Medium,
            color = titleColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = option.description,
            style = HaloType.caption01Regular,
            color = descriptionColor,
            maxLines = 1
        )
    }
}
