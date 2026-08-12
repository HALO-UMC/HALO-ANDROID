package com.umc.halo.presentation.themebox.show_theme

sealed interface ShowThemeUiEvent {
    data object OnClickBackArrow: ShowThemeUiEvent
    data object NextPage: ShowThemeUiEvent
    data object PreviousPage: ShowThemeUiEvent
    data object StopPage: ShowThemeUiEvent
    data object ResumePage: ShowThemeUiEvent
    data object ErrorShown: ShowThemeUiEvent
    data class UpdateProgress(val progress: Float): ShowThemeUiEvent
}