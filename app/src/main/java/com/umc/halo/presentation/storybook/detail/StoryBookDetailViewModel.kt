package com.umc.halo.presentation.storybook.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.umc.halo.core.logging.ErrorReporter
import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.TodayStoryBook
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class StoryBookDetailViewModel @Inject constructor(
    private val storybookDetailRepository: StorybookDetailRepository,
    private val errorReporter: ErrorReporter,
    savedStateHandle: SavedStateHandle
): BaseViewModel<StoryBookDetailUiState, StoryBookDetailUiEvent>(
    StoryBookDetailUiState()
) {
    private val storybookId: Long =
        checkNotNull(savedStateHandle["storybookId"])

    override fun onEvent(event: StoryBookDetailUiEvent) {
        when (event) {
            is StoryBookDetailUiEvent.OnClickDismissDialog -> {
                updateState {
                    copy(
                        showDialog = false
                    )
                }
            }

            is StoryBookDetailUiEvent.OnClickOpenDialog -> {
                updateState {
                    copy(
                        showDialog = true
                    )
                }
            }

            is StoryBookDetailUiEvent.OnClickStoryBookIndex -> {
                if (event.chapterId == 1L) {
                    startStorybook(event.storyBookId)
                }
            }

            is StoryBookDetailUiEvent.OnClickTodayStoryBook -> {
                if (event.chapterId == 1L) {
                    startStorybook(event.storyBookId)
                }
            }

            StoryBookDetailUiEvent.OnScreenShown, StoryBookDetailUiEvent.OnRetryClicked -> getStorybookDetail()

            StoryBookDetailUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }

            else -> Unit
        }
    }

    private fun getStorybookDetail() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            runCatching { storybookDetailRepository.getStorybookDetail(storybookId) }
                .onSuccess { storybookDetail ->
                    updateState {
                        copy(
                            storyBookId = storybookDetail.storyBookId,
                            storyBookInfo = storybookDetail.storyBookInfo,
                            storyBookProgress = storybookDetail.storyBookProgress,
                            storyBookIndex = storybookDetail.storyBookIndex,
                            todayStoryBookInfo = when (val progress = storybookDetail.storyBookProgress) {
                                StorybookProgress.Done ->
                                    storybookDetail.storyBookIndex
                                        .last()
                                        .toTodayStoryBook()
                                        .copy(isCompleted = true)

                                is StorybookProgress.InProgress ->
                                    storybookDetail.storyBookIndex
                                        .first { it.id == progress.chapter + 1L }
                                        .toTodayStoryBook()
                            },
                            hasLoadFailed = false
                        )
                    }
                }.onFailure { throwable ->
                    errorReporter.report(throwable, SCREEN, "get_storybook_detail")
                    updateState {
                        copy(
                            hasLoadFailed = true,
                            errorMessage = LOAD_FAILED_MESSAGE
                        )
                    }
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun startStorybook(storybookId: Long) {
        viewModelScope.launch {
            runCatching {
                storybookDetailRepository.startStorybook(storybookId).storybookId
            }.onSuccess { startedStorybookId ->
                updateState {
                    copy(startedStorybook = startedStorybookId)
                }
            }.onFailure { throwable ->
                handleStartStorybookFailure(storybookId, throwable)
            }
        }
    }

    private fun handleStartStorybookFailure(
        storybookId: Long,
        throwable: Throwable
    ) {
        errorReporter.report(throwable, SCREEN, "start_storybook")
        val apiError = (throwable as? HttpException)?.toApiError()
        if (apiError?.code == ALREADY_STARTED_CODE) {
            updateState {
                copy(startedStorybook = storybookId)
            }
            return
        }

        updateState {
            copy(
                errorMessage = apiError?.message
                    ?: throwable.message?.takeIf { message -> message.isNotBlank() }
                    ?: START_FAILED_MESSAGE
            )
        }
    }

    fun clearStartedStorybook() {
        updateState {
            copy(startedStorybook = null)
        }
    }

    private fun StoryBookIndex.toTodayStoryBook() = TodayStoryBook(
        id = id,
        title = title,
        imageUrl = imageUrl,
        tag = description,
        isLocked = isLocked,
        isCompleted = false
    )

    private companion object {
        const val SCREEN = "storybook_detail"
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val LOAD_FAILED_MESSAGE = "스토리북 상세 페이지를 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
        const val START_FAILED_MESSAGE = "스토리북을 시작하지 못했어요."
        const val ALREADY_STARTED_CODE = "STORYBOOK409_1"
    }
}

private data class StorybookApiError(
    val code: String?,
    val message: String?
)

private fun HttpException.toApiError(): StorybookApiError? =
    response()
        ?.errorBody()
        ?.string()
        ?.let { body ->
            runCatching {
                val json = JSONObject(body)
                StorybookApiError(
                    code = json.optString("code").takeIf { it.isNotBlank() },
                    message = json.extractApiErrorMessage()
                )
            }.getOrNull()
        }

private fun JSONObject.extractApiErrorMessage(): String? {
    val fieldErrors = optJSONObject("result")
    if (fieldErrors != null) {
        val keys = fieldErrors.keys()
        if (keys.hasNext()) {
            return fieldErrors.optString(keys.next()).takeIf { it.isNotBlank() }
        }
    }

    return optString("message").takeIf { it.isNotBlank() }
}
