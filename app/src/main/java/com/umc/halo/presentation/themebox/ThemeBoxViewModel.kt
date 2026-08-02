package com.umc.halo.presentation.themebox

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.nio.file.Files.copy
import javax.inject.Inject

@HiltViewModel
class ThemeBoxViewModel @Inject constructor(
    private val themeBoxRepository: ThemeBoxRepository
): BaseViewModel<ThemeBoxUiState, ThemeBoxUiEvent>(ThemeBoxUiState.Empty.FTU()) {
    override fun onEvent(event: ThemeBoxUiEvent) {
        when (event) {
            is ThemeBoxUiEvent.OnPagerChanged -> {
                updateState {
                    val filledState = this as? ThemeBoxUiState.Filled ?: return@updateState this
                    val themeList = filledState.themeList
                    // 실제 목록 위치로 되돌려 그 테마의 스토리북 id 를 읽음
                    val currentStorybookId = if (themeList.isEmpty()) {
                        null
                    } else {
                        themeList[Math.floorMod(event.page, themeList.size)].storybookId
                    }

                    filledState.copy(currentStorybookId = currentStorybookId)
                }
            }

            else -> Unit
        }
    }

    fun getThemeBox() {
        viewModelScope.launch {
            val themeBox = themeBoxRepository.getThemeBox()

            _uiState.value =
                if (themeBox.numberOfCharacter != 0) {
                    ThemeBoxUiState.Filled(
                        numberOfCharacter = themeBox.numberOfCharacter,
                        storyBookInProgress = themeBox.storyBookInProgress,
                        themeList = themeBox.themeList,
                        currentStorybookId = themeBox.currentStorybookId
                    )
                } else {
                    if (themeBox.storyBookInProgress == 0) {
                        ThemeBoxUiState.Empty.FTU(
                            customStorybookList = themeBox.customStorybookList
                        )
                    } else {
                        ThemeBoxUiState.Empty.RU(
                            continueStorybookList = themeBox.continueStorybookList
                        )
                    }
                }
        }
    }

}
