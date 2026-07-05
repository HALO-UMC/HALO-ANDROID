package com.umc.halo.presentation.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> BaseScreen(
    uiState: UiState<T>,
    onRetry: () -> Unit = {},
    successContent: @Composable (T) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is UiState.Loading -> {
                //로딩 프로그레스 바
            }

            is UiState.Success -> {
                successContent(uiState.data)
            }

            is UiState.Error -> {
                //에러 화면
            }
        }
    }
}