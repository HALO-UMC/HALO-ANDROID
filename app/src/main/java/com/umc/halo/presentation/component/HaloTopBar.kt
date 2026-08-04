package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.HaloType

@Composable
fun HaloTopBar(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = Color(0xFF1D1B20),
    showLeftIcon: Boolean,
    onClick: (() -> Unit) = {}
) {
    Box(
        modifier = modifier
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
                    painter = painterResource(R.drawable.ic_common_chevron_left),
                    contentDescription = "backward",
                    tint = titleColor,
                    modifier = Modifier.size(8.dp, 12.dp)
                )
            }
        }

        //제목
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = HaloType.body01SemiBold,
            color = titleColor
        )
    }
}
