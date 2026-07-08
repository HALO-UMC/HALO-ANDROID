package com.umc.halo.core.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.HaloType

@Composable
fun HaloTopBar(
    title: String,
    navigationIcon: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        //왼쪽 버튼
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            navigationIcon?.invoke()
        }

        //제목
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = HaloType.body01SemiBold
        )
    }
}