package com.umc.halo.presentation.themebox

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeBoxViewModel @Inject constructor(
    private val themeBoxRepository: ThemeBoxRepository
): BaseViewModel<ThemeBoxUiState, ThemeBoxUiEvent>(ThemeBoxUiState()) {

    private var loadJob: Job? = null

    override fun onEvent(event: ThemeBoxUiEvent) {
        when (event) {
            is ThemeBoxUiEvent.OnPagerChanged -> {
                updateState {
                    val currentStorybookId = if (themeList.isEmpty()) {
                        null
                    } else {
                        themeList[Math.floorMod(event.page, themeList.size)].storybookId
                    }

                    copy(currentStorybookId = currentStorybookId)
                }
            }

            ThemeBoxUiEvent.OnThemeBoxShown, ThemeBoxUiEvent.OnRetryClicked -> loadThemeBox()
            ThemeBoxUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }

            else -> Unit
        }
    }

    private fun loadThemeBox() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val isFirstLoad = (currentState.themeList.isEmpty() || currentState.continueStorybookList.isEmpty() || currentState.customStorybookList.isEmpty())
            updateState { copy(isLoading = isFirstLoad, errorMessage = null) }

            runCatching { themeBoxRepository.getThemeBox() }
                .onSuccess {  themeBox ->
                    updateState {
                        copy(
                            themeBoxState = if (themeBox.numberOfCharacter == 0) {
                                if (themeBox.continueStorybookList.isEmpty()) {
                                    ThemeBoxState.Empty.FTU
                                } else {
                                    ThemeBoxState.Empty.RU
                                }
                            } else {
                                ThemeBoxState.Filled
                            },
                            numberOfCharacter = themeBox.numberOfCharacter,
                            storyBookInProgress = themeBox.storyBookInProgress,
                            themeList = themeBox.themeList,
                            currentStorybookId = themeBox.currentStorybookId,
                            customStorybookList = themeBox.customStorybookList,
                            continueStorybookList = themeBox.continueStorybookList,
                            hasLoadFailed = false
                        )
                    }
                }.onFailure {
                    updateState {
                        copy(
                            // 기존 목록이 남아 있으면 그대로 두고 토스트로만 알림
                            hasLoadFailed = currentState.themeList.isEmpty() || currentState.continueStorybookList.isEmpty() || currentState.customStorybookList.isEmpty(),
                            errorMessage = LOAD_FAILED_MESSAGE
                        )
                    }
                }

            updateState { copy(isLoading = false) }
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val LOAD_FAILED_MESSAGE = "테마함을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
    }

}
