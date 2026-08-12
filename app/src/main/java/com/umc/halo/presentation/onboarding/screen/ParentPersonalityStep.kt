package com.umc.halo.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.onboarding.OnboardingTag
import com.umc.halo.presentation.onboarding.MAX_PARENT_PERSONALITY_COUNT
import com.umc.halo.presentation.onboarding.OnboardingUiEvent
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.onboarding.component.OnboardingChoiceChip
import com.umc.halo.presentation.onboarding.component.OnboardingProgressBar
import com.umc.halo.presentation.theme.Error
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParentPersonalityStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    onSystemBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLimitMessage by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(onBack = onSystemBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            OnboardingProgressBar(
                currentStep = 1,
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 19.dp,
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
                    style = HaloType.heading02Regular,
                    color = Gray800
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "해당하는 항목을 모두 선택해주세요. (최대 3개)",
                    style = HaloType.body03Regular,
                    color = Gray400
                )

                val errorMessage = if (showLimitMessage) {
                    "태그는 최대 3개까지 선택할 수 있어요."
                } else {
                    uiState.stepErrorMessage
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = errorMessage,
                        style = HaloType.body03Regular,
                        color = Error
                    )
                }

                Spacer(modifier = Modifier.height(33.dp))

                uiState.parentPersonalityTags.toParentPersonalityGroups()
                    .forEachIndexed { index, group ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        Text(
                            text = group.title,
                            style = HaloType.body02SemiBold,
                            color = Gray700
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            maxItemsInEachRow = 4
                        ) {
                            group.options.forEach { tag ->
                                val isSelected =
                                    tag in uiState.selectedParentPersonalities

                                OnboardingChoiceChip(
                                    text = tag.title,
                                    selected = isSelected,
                                    enabled = true,
                                    onClick = {
                                        when {
                                            isSelected -> {
                                                showLimitMessage = false
                                                onEvent(
                                                    OnboardingUiEvent.ParentPersonalityClicked(
                                                        tag
                                                    )
                                                )
                                            }

                                            uiState.selectedParentPersonalities.size <
                                                    MAX_PARENT_PERSONALITY_COUNT -> {
                                                showLimitMessage = false
                                                onEvent(
                                                    OnboardingUiEvent.ParentPersonalityClicked(
                                                        tag
                                                    )
                                                )
                                            }

                                            else -> {
                                                showLimitMessage = true
                                            }
                                        }
                                    }
                                )
                            }
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

private data class ParentPersonalityUiGroup(
    val title: String,
    val options: List<OnboardingTag>
)

private fun List<OnboardingTag>.toParentPersonalityGroups(): List<ParentPersonalityUiGroup> {
    if (isEmpty()) return emptyList()

    val groups = groupBy { it.subtitle }
    val orderedGroups = listOf(
        "POSITIVE" to "긍정적인 성향",
        "NEUTRAL" to "중립적인 성향",
        "CAUTIOUS" to "신중하게 보는 성향"
    ).mapNotNull { (subtitle, title) ->
        groups[subtitle]?.let { tags ->
            ParentPersonalityUiGroup(
                title = title,
                options = tags
            )
        }
    }

    val ungrouped = groups[null].orEmpty()
    return orderedGroups + if (ungrouped.isEmpty()) {
        emptyList()
    } else {
        listOf(
            ParentPersonalityUiGroup(
                title = "부모님 성향",
                options = ungrouped
            )
        )
    }
}
