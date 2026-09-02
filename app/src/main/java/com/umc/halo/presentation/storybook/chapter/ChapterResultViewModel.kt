package com.umc.halo.presentation.storybook.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.halo.core.logging.ErrorReporter
import com.umc.halo.domain.repository.chapter.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterResultViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val errorReporter: ErrorReporter
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChapterResultUiState())
    val uiState: StateFlow<ChapterResultUiState> = _uiState.asStateFlow()

    fun loadCompletedChapter(memberChapterId: Long) {
        val current = _uiState.value.completedChapter
        if (current?.memberChapterId == memberChapterId) {
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                chapterRepository.getCompletedChapter(memberChapterId)
            }.onSuccess { completedChapter ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        completedChapter = completedChapter
                    )
                }
            }.onFailure { throwable ->
                errorReporter.report(throwable, SCREEN, "load_completed_chapter")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "완료된 장을 불러오지 못했어요."
                    )
                }
            }
        }
    }

    private companion object {
        const val SCREEN = "chapter_result"
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
