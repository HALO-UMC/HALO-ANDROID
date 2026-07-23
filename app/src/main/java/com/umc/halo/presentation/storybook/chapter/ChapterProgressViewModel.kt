package com.umc.halo.presentation.storybook.chapter

import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterStatus
import com.umc.halo.presentation.base.BaseViewModel

class ChapterProgressViewModel :
    BaseViewModel<ChapterProgressUiState, ChapterProgressUiEvent>(
        initialState = ChapterProgressUiState()
    ) {

    override fun onEvent(event: ChapterProgressUiEvent) {
        when (event) {
            is ChapterProgressUiEvent.Initialize -> {
                initializeChapter(
                    storybookId = event.storybookId,
                    chapterId = event.chapterId
                )
            }

            ChapterProgressUiEvent.NextClicked -> {
                moveToNextStep()
            }

            ChapterProgressUiEvent.BackClicked -> {
                moveToPreviousStep()
            }
        }
    }

    private fun initializeChapter(
        storybookId: Long,
        chapterId: Long
    ) {
        val currentChapter = currentState.chapter

        if (
            currentState.isInitialized &&
            currentChapter?.storybookId == storybookId &&
            currentChapter?.id == chapterId
        ) {
            return
        }

        updateState {
            copy(
                isInitialized = true,
                chapter = createDummyChapter(
                    storybookId = storybookId,
                    chapterId = chapterId
                ),
                currentStep = ChapterProgressStep.INTRO
            )
        }
    }

    private fun moveToNextStep() {
        updateState {
            copy(currentStep = currentStep.next())
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(currentStep = currentStep.previous())
        }
    }

    private fun createDummyChapter(
        storybookId: Long,
        chapterId: Long
    ): Chapter {
        val chapterNumber = chapterId
            .toInt()
            .coerceIn(
                minimumValue = 1,
                maximumValue = 10
            )

        return when (chapterNumber) {
            1 -> Chapter(
                id = chapterId,
                storybookId = storybookId,
                storybookTitle = "오래전 당신",
                number = 1,
                title = "나와 같은 나이였던 시절",
                description = "부모님을 한 사람으로 바라보는 첫 장입니다.\n" +
                        "지금의 내 나이였을 때 부모님은 어떤 하루를 살고\n" +
                        "있었는지 들어봅니다.",
                backgroundImageUrl = null,
                guideImageUrl = null,
                themeGuideText = "지금의 부모님도 한때는\n지금 나의 나이로 하루를 살고 있었어요.",
                chapterGuideText = "부모님은 처음 어떻게 만나셨을까요?\n" +
                        "첫인상부터 조심스럽게 물어보며 가족의\n" +
                        "시작을 떠올려봐요!",
                status = ChapterStatus.AVAILABLE
            )

            else -> Chapter(
                id = chapterId,
                storybookId = storybookId,
                storybookTitle = "오래전 당신",
                number = chapterNumber,
                title = "${chapterNumber}번째 이야기",
                description = "부모님의 이야기를 차근차근 기록하는 챕터입니다.",
                backgroundImageUrl = null,
                guideImageUrl = null,
                themeGuideText = "지금의 부모님도 한때는\n지금 나의 나이로 하루를 살고 있었어요.",
                chapterGuideText = "부모님의 이야기를 천천히 떠올려볼까요?",
                status = ChapterStatus.AVAILABLE
            )
        }
    }
}