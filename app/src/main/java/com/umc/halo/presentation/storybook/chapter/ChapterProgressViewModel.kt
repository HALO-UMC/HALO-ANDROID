package com.umc.halo.presentation.storybook.chapter

import androidx.lifecycle.viewModelScope
import com.umc.halo.core.logging.ActionReporter
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterCoverType
import com.umc.halo.domain.model.storybook.ChapterDraft
import com.umc.halo.domain.model.storybook.ChapterSaveAnswer
import com.umc.halo.domain.model.storybook.ChapterSaveForm
import com.umc.halo.domain.model.storybook.ChapterSaveStatus
import com.umc.halo.domain.repository.chapter.ChapterRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterProgressViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val actionReporter: ActionReporter
) : BaseViewModel<ChapterProgressUiState, ChapterProgressUiEvent>(
    initialState = ChapterProgressUiState()
) {
    private var draftSaveJob: Job? = null

    override fun onEvent(event: ChapterProgressUiEvent) {
        when (event) {
            is ChapterProgressUiEvent.Initialize -> {
                initializeChapter(
                    storybookId = event.storybookId,
                    chapterOrder = event.chapterOrder
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
                uploadSceneImage(event.imageUri)
            }

            ChapterProgressUiEvent.SceneCardModalRequested -> {
                openSceneCardModal()
            }

            ChapterProgressUiEvent.SceneCardModalDismissed -> {
                closeSceneCardModal()
            }

            is ChapterProgressUiEvent.PendingSceneCardSelected -> {
                updatePendingSceneCard(event.cardId)
            }

            ChapterProgressUiEvent.SceneCardConfirmed -> {
                confirmSceneCard()
            }

            ChapterProgressUiEvent.SceneCardChangeClicked -> {
                openSceneCardModalFromConfirm()
            }

            is ChapterProgressUiEvent.MoodSelected -> {
                updateMood(event.mood)
            }

            ChapterProgressUiEvent.NextClicked -> {
                moveToNextStep()
            }

            ChapterProgressUiEvent.CompleteClicked -> {
                completeChapter()
            }

            ChapterProgressUiEvent.BackClicked -> {
                moveToPreviousStep()
            }

            ChapterProgressUiEvent.ErrorShown -> {
                updateState { copy(errorMessage = null) }
            }

            ChapterProgressUiEvent.NavigationHandled -> {
                updateState { copy(navigateToStorybookDetail = null) }
            }
        }
    }

    private fun initializeChapter(
        storybookId: Long,
        chapterOrder: Int
    ) {
        val currentChapter = currentState.chapter
        if (
            currentState.isInitialized &&
            currentChapter?.storybookId == storybookId &&
            currentChapter.number == chapterOrder
        ) {
            return
        }

        updateState {
            copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                chapterRepository.getTodayChapter(storybookId, chapterOrder)
            }.onSuccess { todayChapter ->
                actionReporter.reportSuccess(SCREEN, "init")
                val chapter = todayChapter.chapter
                val restoredState = currentState.applyDraft(
                    chapter = chapter,
                    draft = todayChapter.draft
                )
                updateState {
                    restoredState.copy(
                        isInitialized = true,
                        isLoading = false,
                        chapter = chapter,
                        sceneCards = todayChapter.sceneCards,
                        currentStep = ChapterProgressStep.INTRO
                    )
                }
            }.onFailure { throwable ->
                actionReporter.reportFailure(throwable, SCREEN, "init")
                updateState {
                    copy(
                        isInitialized = false,
                        isLoading = false,
                        errorMessage = throwable.message ?: "오늘의 장을 불러오지 못했어요."
                    )
                }
            }
        }
    }

    private fun ChapterProgressUiState.applyDraft(
        chapter: Chapter,
        draft: ChapterDraft
    ): ChapterProgressUiState {
        val answers = List(chapter.questions.size) { index ->
            val question = chapter.questions[index]
            draft.answers.firstOrNull { it.chapterQuestionId == question.id }?.answer
                ?: draft.answers.firstOrNull { it.questionOrder == question.order }?.answer
                ?: ""
        }

        val selectedMethod = when (draft.coverType) {
            ChapterCoverType.IMAGE -> ChapterSceneRecordMethod.PHOTO
            ChapterCoverType.SCENE_CARD -> ChapterSceneRecordMethod.SCENE_CARD
            null -> null
        }

        return copy(
            questionAnswers = answers,
            selectedSceneRecordMethod = selectedMethod,
            selectedSceneImageUri = draft.imageUrl,
            selectedSceneImageKey = draft.imageKey,
            selectedSceneCardId = draft.sceneCardId,
            pendingSceneCardId = draft.sceneCardId,
            selectedMood = ChapterMood.fromEmotion(draft.emotion)
        )
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
            copy(selectedSceneRecordMethod = method)
        }
    }

    private fun uploadSceneImage(
        imageUri: String
    ) {
        updateState {
            copy(
                selectedSceneRecordMethod = ChapterSceneRecordMethod.PHOTO,
                selectedSceneImageUri = imageUri,
                selectedSceneImageKey = null,
                selectedSceneCardId = null,
                pendingSceneCardId = null,
                isSceneCardModalVisible = false,
                isImageUploading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                chapterRepository.uploadImageFromUri(imageUri)
            }.onSuccess { uploadedImage ->
                actionReporter.reportSuccess(SCREEN, "upload_image")
                updateState {
                    copy(
                        selectedSceneImageKey = uploadedImage.imageKey,
                        isImageUploading = false,
                        currentStep = ChapterProgressStep.SCENE_CONFIRM
                    )
                }
            }.onFailure { throwable ->
                actionReporter.reportFailure(throwable, SCREEN, "upload_image")
                updateState {
                    copy(
                        isImageUploading = false,
                        selectedSceneImageUri = null,
                        errorMessage = throwable.message ?: "사진 업로드에 실패했어요. 다시 선택해주세요."
                    )
                }
            }
        }
    }

    private fun openSceneCardModal() {
        updateState {
            copy(
                selectedSceneRecordMethod = ChapterSceneRecordMethod.SCENE_CARD,
                pendingSceneCardId = selectedSceneCardId,
                isSceneCardModalVisible = true
            )
        }
    }

    private fun closeSceneCardModal() {
        updateState {
            copy(
                selectedSceneRecordMethod = committedSceneRecordMethod(),
                pendingSceneCardId = selectedSceneCardId,
                isSceneCardModalVisible = false
            )
        }
    }

    private fun ChapterProgressUiState.committedSceneRecordMethod(): ChapterSceneRecordMethod? {
        return when {
            !selectedSceneImageKey.isNullOrBlank() -> ChapterSceneRecordMethod.PHOTO
            selectedSceneCardId != null -> ChapterSceneRecordMethod.SCENE_CARD
            else -> null
        }
    }

    private fun updatePendingSceneCard(
        cardId: Long
    ) {
        updateState {
            copy(pendingSceneCardId = cardId)
        }
    }

    private fun confirmSceneCard() {
        val selectedCardId = currentState.pendingSceneCardId ?: return

        updateState {
            copy(
                selectedSceneRecordMethod = ChapterSceneRecordMethod.SCENE_CARD,
                selectedSceneCardId = selectedCardId,
                selectedSceneImageUri = null,
                selectedSceneImageKey = null,
                isSceneCardModalVisible = false,
                currentStep = ChapterProgressStep.SCENE_CONFIRM
            )
        }
    }

    private fun openSceneCardModalFromConfirm() {
        updateState {
            copy(
                currentStep = ChapterProgressStep.SCENE,
                selectedSceneRecordMethod = ChapterSceneRecordMethod.SCENE_CARD,
                pendingSceneCardId = selectedSceneCardId,
                isSceneCardModalVisible = true
            )
        }
    }

    private fun updateMood(
        mood: ChapterMood
    ) {
        updateState {
            copy(
                selectedMood = if (selectedMood == mood) {
                    null
                } else {
                    mood
                }
            )
        }
    }

    private fun moveToNextStep() {
        val state = currentState
        if (state.isSaving || state.isImageUploading) {
            return
        }

        if (
            state.currentStep == ChapterProgressStep.QUESTION &&
            !state.isQuestionStepNextEnabled
        ) {
            return
        }

        if (
            state.currentStep == ChapterProgressStep.SCENE &&
            !state.isSceneStepNextEnabled
        ) {
            return
        }

        if (
            state.currentStep == ChapterProgressStep.MOOD &&
            !state.isMoodStepNextEnabled
        ) {
            return
        }

        when (state.currentStep) {
            ChapterProgressStep.QUESTION,
            ChapterProgressStep.SCENE_CONFIRM,
            ChapterProgressStep.MOOD -> saveDraft()
            else -> Unit
        }

        updateState {
            copy(currentStep = currentStep.next())
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(
                currentStep = currentStep.previous(),
                isSceneCardModalVisible = false
            )
        }
    }

    private fun saveDraft() {
        val form = currentState.toSaveForm(ChapterSaveStatus.DRAFT) ?: return

        draftSaveJob?.cancel()
        draftSaveJob = viewModelScope.launch {
            runCatching {
                chapterRepository.saveMemberChapter(form)
            }.onSuccess {
                actionReporter.reportSuccess(SCREEN, "save_draft")
            }.onFailure { throwable ->
                actionReporter.reportFailure(throwable, SCREEN, "save_draft")
                updateState {
                    copy(errorMessage = throwable.message ?: "임시저장에 실패했어요.")
                }
            }
        }
    }

    private fun completeChapter() {
        if (currentState.isSaving) {
            return
        }

        val form = currentState.toSaveForm(ChapterSaveStatus.COMPLETED) ?: return
        draftSaveJob?.cancel()

        updateState {
            copy(
                isSaving = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                chapterRepository.saveMemberChapter(form)
            }.onSuccess {
                actionReporter.reportSuccess(SCREEN, "complete")
                val storybookId = currentState.chapter?.storybookId
                updateState {
                    copy(
                        isSaving = false,
                        navigateToStorybookDetail = storybookId
                    )
                }
            }.onFailure { throwable ->
                actionReporter.reportFailure(throwable, SCREEN, "complete")
                updateState {
                    copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "장 기록을 저장하지 못했어요."
                    )
                }
            }
        }
    }

    private fun ChapterProgressUiState.toSaveForm(
        status: ChapterSaveStatus
    ): ChapterSaveForm? {
        val chapter = chapter ?: return null
        val answers = chapter.questions.mapIndexedNotNull { index, question ->
            val answer = questionAnswers.getOrElse(index) { "" }.trim()
            if (answer.isBlank()) {
                null
            } else {
                ChapterSaveAnswer(
                    chapterQuestionId = question.id,
                    answer = answer
                )
            }
        }

        return ChapterSaveForm(
            chapterId = chapter.id,
            emotion = selectedMood?.toEmotion(),
            coverType = coverType,
            imageKey = selectedSceneImageKey,
            sceneCardId = selectedSceneCardId,
            answers = answers,
            status = status
        )
    }

    private companion object {
        const val SCREEN = "chapter_progress"
    }
}
