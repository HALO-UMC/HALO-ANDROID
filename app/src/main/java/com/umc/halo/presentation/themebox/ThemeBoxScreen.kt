package com.umc.halo.presentation.themebox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * @param initialStorybookId 특정 스토리북으로 진입한 경우 그 테마부터 보여줌
 *   하단바로 테마함 탭을 누른 경우처럼 지정이 없으면 null → 첫 번째 테마
 */
@Composable
fun ThemeBoxRoute(
    viewModel: ThemeBoxViewModel = hiltViewModel(),
    initialStorybookId: Long? = null,
    onNavigateToStorybook: (Long) -> Unit,
    onNavigateToShowTheme: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ThemeBoxScreen(
        state = state,
        initialStorybookId = initialStorybookId,
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
    initialStorybookId: Long? = null,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    when (state) {
        is ThemeBoxUiState.Filled -> {
            ThemeBoxFilledScreen(state, initialStorybookId, onEvent)
        }
        is ThemeBoxUiState.Empty -> {
            ThemeBoxEmptyScreen(state, onEvent)
        }
    }
}
