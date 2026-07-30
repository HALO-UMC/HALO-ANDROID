package com.umc.halo.presentation.themebox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ThemeBoxRoute(
    viewModel: ThemeBoxViewModel = hiltViewModel(),
    onNavigateToStorybook: (Long) -> Unit,
    onNavigateToShowTheme: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ThemeBoxScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is ThemeBoxUiEvent.OnContinueStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }

                is ThemeBoxUiEvent.OnCustomizedStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }

                is ThemeBoxUiEvent.OnShowThemeClicked -> {
                    onNavigateToShowTheme(event.storyBookId)
                }

                is ThemeBoxUiEvent.OnPagerChanged -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}

@Composable
fun ThemeBoxScreen(
    state: ThemeBoxUiState,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    when (state) {
        is ThemeBoxUiState.Filled -> {
            ThemeBoxFilledScreen(state, onEvent)
        }
        is ThemeBoxUiState.Empty -> {
            ThemeBoxEmptyScreen(state, onEvent)
        }
    }
}
