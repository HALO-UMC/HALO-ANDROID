package com.umc.halo.presentation.themebox.show_theme

sealed interface ShowThemeUiEvent {
    data object OnClickBackArrow: ShowThemeUiEvent
}