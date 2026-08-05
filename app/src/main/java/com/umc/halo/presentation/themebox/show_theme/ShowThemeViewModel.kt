package com.umc.halo.presentation.themebox.show_theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import com.umc.halo.presentation.base.BaseViewModel
import com.umc.halo.presentation.base.UiState
import com.umc.halo.presentation.themebox.ThemeBoxUiEvent
import com.umc.halo.presentation.themebox.ThemeBoxUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.nio.file.Files.copy
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

            else -> Unit
        }
    }

    fun getThemeExhibition() {
        viewModelScope.launch {
            val themeExhibition = themeBoxRepository.getThemeExhibition(storybookId)

            updateState {
                copy(
                    storybookId = themeExhibition.storybookId,
                    chapters = themeExhibition.chapters.map {
                        ThemeExhibitionChapter(
                            id = it.chapterOrder,
                            title = it.title,
                            imageUrl = it.chapterImageUrl,
                            completedDate = it.completedDate,
                            summary = it.summary
                        )
                    }
                )
            }
        }
    }

}