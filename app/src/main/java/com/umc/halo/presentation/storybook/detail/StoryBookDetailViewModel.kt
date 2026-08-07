package com.umc.halo.presentation.storybook.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.TodayStoryBook
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.collections.listOf

@HiltViewModel
class StoryBookDetailViewModel @Inject constructor(
    private val storybookDetailRepository: StorybookDetailRepository,
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

            else -> Unit
        }
    }

    fun getStorybookDetail() {
        viewModelScope.launch {
            val storybookDetail = storybookDetailRepository.getStorybookDetail(storybookId)

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
                    }
                )
            }
        }
    }

    fun startStorybook(storybookId: Long) {
        viewModelScope.launch {
            val startStoryBookId = runCatching {
                storybookDetailRepository.startStorybook(storybookId).storybookId
            }.recoverCatching { throwable ->
                if (throwable is HttpException && throwable.code() == 409) {
                    storybookId
                } else {
                    throw throwable
                }
            }.getOrNull() ?: return@launch

            updateState {
                copy(
                    startedStorybook = startStoryBookId
                )
            }
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
}
