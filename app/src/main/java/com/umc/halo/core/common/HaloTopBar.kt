package com.umc.halo.core.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.HaloType

@Composable
fun HaloTopBar(
    title: String,
    showLeftIcon: Boolean,
    onClick: (() -> Unit) = {}
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(56.dp)
    ) {
        //왼쪽 버튼
        if(showLeftIcon) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp),
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_backward_arrow),
                    contentDescription = "backward"
                )
            }
        }

        //제목
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = HaloType.body01SemiBold
        )
    }
}