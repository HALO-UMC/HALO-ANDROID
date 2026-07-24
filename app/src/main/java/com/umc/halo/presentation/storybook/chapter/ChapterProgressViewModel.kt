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

            is ChapterProgressUiEvent.QuestionAnswerChanged -> {
                updateQuestionAnswer(
                    questionIndex = event.index,
                    answer = event.answer
                )
            }

            is ChapterProgressUiEvent.SceneRecordMethodSelected -> {
                updateSceneRecordMethod(event.method)
            }

            is ChapterProgressUiEvent.SceneImageSelected -> {
                updateSceneImage(event.imageUri)
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
            currentChapter.id == chapterId
        ) {
            return
        }

        val chapter = createDummyChapter(
            storybookId = storybookId,
            chapterId = chapterId
        )

        updateState {
            copy(
                isInitialized = true,
                chapter = chapter,
                currentStep = ChapterProgressStep.INTRO,
                questionAnswers = List(chapter.questions.size) { "" },
                selectedSceneRecordMethod = null,
                selectedSceneImageUri = null,
                selectedSceneCardId = null
            )
        }
    }

    private fun updateQuestionAnswer(
        questionIndex: Int,
        answer: String
    ) {
        if (questionIndex !in currentState.questionAnswers.indices) {
            return
        }

        val updatedAnswers = currentState.questionAnswers.toMutableList().apply {
            this[questionIndex] = answer
        }

        updateState {
            copy(questionAnswers = updatedAnswers)
        }
    }

    private fun updateSceneRecordMethod(
        method: ChapterSceneRecordMethod
    ) {
        updateState {
            copy(
                selectedSceneRecordMethod = method
            )
        }
    }

    private fun updateSceneImage(
        imageUri: String
    ) {
        updateState {
            copy(
                selectedSceneRecordMethod = ChapterSceneRecordMethod.PHOTO,
                selectedSceneImageUri = imageUri,
                selectedSceneCardId = null,
                currentStep = ChapterProgressStep.SCENE_CONFIRM
            )
        }
    }

    private fun moveToNextStep() {
        if (
            currentState.currentStep == ChapterProgressStep.QUESTION &&
            !currentState.isQuestionStepNextEnabled
        ) {
            return
        }

        if (
            currentState.currentStep == ChapterProgressStep.SCENE &&
            !currentState.isSceneSelected
        ) {
            return
        }

        updateState {
            copy(
                currentStep = currentStep.next()
            )
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(
                currentStep = currentStep.previous()
            )
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
                themeGuideText = "지금의 부모님도 한때는\n" +
                        "지금 나의 나이로 하루를 살고 있었어요.",
                chapterGuideText = "부모님은 처음 어떻게 만나셨을까요?\n" +
                        "첫인상부터 조심스럽게 물어보며 가족의\n" +
                        "시작을 떠올려봐요!",
                questions = listOf(
                    "그 시절 부모님의 나이를 기록해보세요.",
                    "어디에 살고 계셨나요?",
                    "어떤 일을 하고 계셨나요?"
                ),
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
                themeGuideText = "지금의 부모님도 한때는\n" +
                        "지금 나의 나이로 하루를 살고 있었어요.",
                chapterGuideText = "부모님의 이야기를 천천히 떠올려볼까요?",
                questions = listOf(
                    "그날 부모님은 어디에 계셨나요?",
                    "가장 기억에 남는 순간은 무엇인가요?",
                    "그때 어떤 마음이셨을까요?"
                ),
                status = ChapterStatus.AVAILABLE
            )
        }
    }
}