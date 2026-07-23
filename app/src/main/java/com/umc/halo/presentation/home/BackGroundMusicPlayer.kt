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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30

@Composable
fun BackGroundMusicPlayer(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .aspectRatio(52f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .background(if (isPlaying) Primary30 else Gray30)
            .padding(
                horizontal = 18.dp,
                vertical = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                append("산들바람 ")

                withStyle(
                    style = HaloType.body03Regular.toSpanStyle()
                ) {
                    append("Track01")
                }
            },
            style = HaloType.body02Medium,
            color = Gray600
        )

        Spacer(Modifier.weight(1f))

        Box(
            Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (isPlaying) {
                Icon(
                    painter = painterResource(R.drawable.ic_home_bgmplayer_pause),
                    contentDescription = "bgm_pause"
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_home_bgmplayer_play),
                    contentDescription = "bgm_play"
                )
            }
        }
    }
}