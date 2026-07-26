package com.umc.halo.presentation.themebox.show_theme

import com.umc.halo.presentation.base.BaseViewModel
import com.umc.halo.presentation.themebox.ThemeBoxUiEvent
import com.umc.halo.presentation.themebox.ThemeBoxUiState

class ShowThemeViewModel: BaseViewModel<ShowThemeUiState, ShowThemeUiEvent>(ShowThemeUiState()) {
    override fun onEvent(event: ShowThemeUiEvent) {}

}