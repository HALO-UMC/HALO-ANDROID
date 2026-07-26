package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White

@Composable
fun SystemVolumeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val sliderWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val coercedValue = value.coerceIn(0f, 1f)
        val thumbSize = 12.dp
        val trackWidth = maxWidth

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .pointerInput(sliderWidthPx) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            onValueChange((offset.x / sliderWidthPx).coerceIn(0f, 1f))
                        },
                        onDrag = { change, _ ->
                            onValueChange((change.position.x / sliderWidthPx).coerceIn(0f, 1f))
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Gray200)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(coercedValue)
                    .height(3.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Gray600)
            )
            Box(
                modifier = Modifier
                    .offset(x = (trackWidth - thumbSize) * coercedValue)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(Gray600)
            )
        }
    }
}

@Composable
fun TrackRow(
    title: String,
    selected: Boolean,
    playing: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Primary100)
            )
            Spacer(Modifier.width(14.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(title)
                    }
                    append("  ")
                    withStyle(
                        SpanStyle(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Track 01")
                    }
                },
                style = HaloType.body02Medium,
                color = if (selected) Primary600 else Gray700,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(
                    id = if (playing) {
                        R.drawable.ic_home_bgmplayer_pause
                    } else {
                        R.drawable.ic_home_bgmplayer_play
                    }
                ),
                contentDescription = null,
                tint = if (selected) Primary600 else Gray800,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
