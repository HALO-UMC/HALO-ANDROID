package com.umc.halo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary600

@Composable
fun BackGroundMusicPlayer(
    title: String,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .aspectRatio(52f / 7f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .background(if (isPlaying) Primary50 else Gray30)
            .padding(
                horizontal = 18.dp,
                vertical = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = HaloType.body02Medium,
            color = if (isPlaying) Primary600 else Gray700
        )

        Spacer(Modifier.weight(1f))

        Box(
            Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (isPlaying) {
                        R.drawable.ic_home_bgmplayer_pause
                    } else {
                        R.drawable.ic_home_bgmplayer_play
                    }
                ),
                tint = if (isPlaying) Primary600 else Gray700,
                contentDescription = if (isPlaying) "bgm_pause" else "bgm_play"
            )
        }
    }
}
