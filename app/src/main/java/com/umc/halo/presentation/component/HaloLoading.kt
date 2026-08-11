package com.umc.halo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.umc.halo.presentation.theme.Primary500

/**
 * 첫 조회 중
 * TODO: 로딩 표현은 디자인에 없어 임시
 */
@Composable
fun HaloLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Primary500)
    }
}