package com.umc.halo.presentation.themebox.show_theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.umc.halo.core.network.toUserMessageOrNull
import com.umc.halo.domain.model.showTheme.ThemeExhibitionChapter
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowThemeViewModel @Inject constructor(
    private val themeBoxRepository: ThemeBoxRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel<ShowThemeUiState, ShowThemeUiEvent>(ShowThemeUiState()) {

    private val storybookId: Long =
        checkNotNull(savedStateHandle["storybookId"])

    override fun onEvent(event: ShowThemeUiEvent) {
        when (event) {
            ShowThemeUiEvent.PreviousPage -> {
                updateState {
                    copy(
                        currentPage = (currentPage - 1).coerceAtLeast(0),
                        progress = 0f
                    )
                }
            }

            ShowThemeUiEvent.NextPage -> {
                updateState {
                    copy(
                        currentPage = (currentPage + 1).coerceAtMost(chapters.lastIndex),
                        progress = 0f
                    )
                }
            }

            ShowThemeUiEvent.StopPage -> {
                updateState {
                    copy(
                        isPlaying = false
                    )
                }
            }

            ShowThemeUiEvent.ResumePage -> {
                updateState {
                    copy(
                        isPlaying = true
                    )
                }
            }

            is ShowThemeUiEvent.UpdateProgress -> {
                updateState {
                    copy(
                        progress = event.progress
                            .coerceIn(0f, 1f)
                    )
                }
            }

            ShowThemeUiEvent.ErrorShown ->  updateState { copy(errorMessage = null) }

            else -> Unit
        }
    }

    fun getThemeExhibition() {
        viewModelScope.launch {
            updateState { copy(errorMessage = null) }
            runCatching {
                themeBoxRepository.getThemeExhibition(storybookId)
            }.onSuccess { themeExhibition ->
                updateState {
                    copy(
                        storybookId = themeExhibition.storybookId,
                        chapters = themeExhibition.chapters
                    )
                }
            }.onFailure { throwable ->
                // 레포지토리가 네트워크 단절, 서버 오류를 구분해 문구를 만들어 줌
                updateState {
                    copy(errorMessage = throwable.toUserMessageOrNull() ?: LOAD_FAILED_MESSAGE)
                }
            }
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val LOAD_FAILED_MESSAGE = "감상화면을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
    }

}