package com.umc.halo.presentation.storybook.chapter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomButton
import com.umc.halo.presentation.storybook.chapter.screen.ChapterGuideStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterIntroStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterMoodStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterQuestionStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterReviewStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterSceneStep
import com.umc.halo.presentation.storybook.chapter.screen.ChapterSceneConfirmStep
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

/**
 * NavGraph에서 호출하는 챕터 작성 화면 진입점
 */
@Composable
fun ChapterProgressRoute(
    storybookId: Long,
    chapterOrder: Int,
    onNavigateBack: () -> Unit,
    onNavigateToStorybookDetail: (Long) -> Unit,
    vm: ChapterProgressViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = storybookId,
        key2 = chapterOrder
    ) {
        vm.onEvent(
            ChapterProgressUiEvent.Initialize(
                storybookId = storybookId,
                chapterOrder = chapterOrder
            )
        )
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        vm.onEvent(ChapterProgressUiEvent.ErrorShown)
    }

    LaunchedEffect(state.navigateToStorybookDetail) {
        val targetStorybookId = state.navigateToStorybookDetail ?: return@LaunchedEffect
        vm.onEvent(ChapterProgressUiEvent.NavigationHandled)
        onNavigateToStorybookDetail(targetStorybookId)
    }

    ChapterProgressScreen(
        state = state,
        onEvent = vm::onEvent,
        onNavigateBack = onNavigateBack
    )
}

/**
 * 챕터 작성 내부 단계에 따라 화면을 교체합니다.
 */
@Composable
private fun ChapterProgressScreen(
    state: ChapterProgressUiState,
    onEvent: (ChapterProgressUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val chapter = state.chapter

    val handleBack: () -> Unit = {
        if (state.isFirstStep) {
            onNavigateBack()
        } else {
            onEvent(ChapterProgressUiEvent.BackClicked)
        }
    }

    BackHandler {
        handleBack()
    }

    if (!state.isInitialized || chapter == null) {
        ChapterLoadingScreen()
        return
    }

    when (state.currentStep) {
        ChapterProgressStep.INTRO -> {
            ChapterIntroStep(
                chapter = chapter,
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.GUIDE -> {
            ChapterGuideStep(
                chapter = chapter,
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.QUESTION -> {
            ChapterQuestionStep(
                chapter = chapter,
                answers = state.questionAnswers,
                isNextEnabled = state.isQuestionStepNextEnabled,
                onAnswerChanged = { questionIndex, answer ->
                    onEvent(
                        ChapterProgressUiEvent.QuestionAnswerChanged(
                            index = questionIndex,
                            answer = answer
                        )
                    )
                },
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.SCENE -> {
            ChapterSceneStep(
                chapter = chapter,
                sceneCards = state.sceneCards,
                selectedMethod = state.selectedSceneRecordMethod,
                pendingSceneCardId = state.pendingSceneCardId,
                isSceneCardModalVisible = state.isSceneCardModalVisible,
                isSceneCardConfirmEnabled = state.isSceneCardConfirmEnabled,
                isNextEnabled = state.isSceneStepNextEnabled,
                onMethodSelected = { method ->
                    onEvent(
                        ChapterProgressUiEvent.SceneRecordMethodSelected(
                            method = method
                        )
                    )
                },
                onImageSelected = { imageUri ->
                    onEvent(
                        ChapterProgressUiEvent.SceneImageSelected(
                            imageUri = imageUri
                        )
                    )
                },
                onSceneCardModalRequested = {
                    onEvent(ChapterProgressUiEvent.SceneCardModalRequested)
                },
                onSceneCardModalDismissed = {
                    onEvent(ChapterProgressUiEvent.SceneCardModalDismissed)
                },
                onPendingSceneCardSelected = { cardId ->
                    onEvent(
                        ChapterProgressUiEvent.PendingSceneCardSelected(
                            cardId = cardId
                        )
                    )
                },
                onSceneCardConfirmClick = {
                    onEvent(ChapterProgressUiEvent.SceneCardConfirmed)
                },
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.SCENE_CONFIRM -> {
            ChapterSceneConfirmStep(
                chapter = chapter,
                selectedMethod = state.selectedSceneRecordMethod,
                selectedImageUri = state.selectedSceneImageUri,
                selectedSceneCard = state.selectedSceneCard,
                onImageSelected = { imageUri ->
                    onEvent(
                        ChapterProgressUiEvent.SceneImageSelected(
                            imageUri = imageUri
                        )
                    )
                },
                onSceneCardChangeClick = {
                    onEvent(ChapterProgressUiEvent.SceneCardChangeClicked)
                },
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.MOOD -> {
            ChapterMoodStep(
                chapter = chapter,
                selectedMood = state.selectedMood,
                isNextEnabled = state.isMoodStepNextEnabled,
                onMoodClick = { mood ->
                    onEvent(
                        ChapterProgressUiEvent.MoodSelected(
                            mood = mood
                        )
                    )
                },
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }

        ChapterProgressStep.REVIEW -> {
            ChapterReviewStep(
                chapter = chapter,
                answers = state.questionAnswers,
                selectedMethod = state.selectedSceneRecordMethod,
                selectedImageUri = state.selectedSceneImageUri,
                selectedSceneCard = state.selectedSceneCard,
                selectedMood = state.selectedMood,
                onBackClick = handleBack,
                onCompleteClick = {
                    onEvent(ChapterProgressUiEvent.CompleteClicked)
                }
            )
        }

        else -> {
            ChapterTemporaryStep(
                chapter = chapter,
                currentStep = state.currentStep,
                onBackClick = handleBack,
                onNextClick = {
                    onEvent(ChapterProgressUiEvent.NextClicked)
                }
            )
        }
    }
}

@Composable
private fun ChapterLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * 아직 구현하지 않은 후속 단계를 확인하기 위한 임시 화면
 */
@Composable
private fun ChapterTemporaryStep(
    chapter: Chapter,
    currentStep: ChapterProgressStep,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HaloTopBar(
            title = chapter.storybookTitle,
            showLeftIcon = true,
            onClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentStep.displayName,
                style = HaloType.body01SemiBold,
                color = Gray800
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "다음 단계의 UI는 이후 순서대로 구현합니다.",
                style = HaloType.body03Regular,
                color = Gray500
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (currentStep != ChapterProgressStep.REVIEW) {
                ChapterBottomButton(
                    text = "다음",
                    enabled = true,
                    onClick = onNextClick
                )
            }
        }
    }
}
