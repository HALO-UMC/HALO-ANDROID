package com.umc.halo.presentation.themebox

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.presentation.storybook.chapter.component.HaloLoadFailed
import com.umc.halo.presentation.storybook.chapter.component.HaloLoading

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
    LaunchedEffect(Unit) {
        viewModel.onEvent(ThemeBoxUiEvent.OnThemeBoxShown)
    }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(ThemeBoxUiEvent.ErrorShown)
    }

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

                else -> viewModel.onEvent(event)
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
    when {
        state.isLoading -> HaloLoading()

        state.hasLoadFailed -> HaloLoadFailed(
            text = "테마함",
            onRetry = { onEvent(ThemeBoxUiEvent.OnRetryClicked) }
        )

        else -> {
            when (state.themeBoxState) {
                ThemeBoxState.Filled -> {
                    ThemeBoxFilledScreen(state, initialStorybookId, onEvent)
                }
                ThemeBoxState.Empty.RU, ThemeBoxState.Empty.FTU -> {
                    ThemeBoxEmptyScreen(state, onEvent)
                }
            }
        }
    }
}
