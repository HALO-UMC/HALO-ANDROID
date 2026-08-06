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
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.dp
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
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(14.dp)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = value.coerceIn(0f, 1f),
                    range = 0f..1f
                )
                setProgress { targetValue ->
                    onValueChange(targetValue.coerceIn(0f, 1f))
                    onValueChangeFinished()
                    true
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val sliderWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val coercedValue = value.coerceIn(0f, 1f)
        val thumbSize = 14.dp
        val trackWidth = maxWidth

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .pointerInput(sliderWidthPx) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            onValueChange((offset.x / sliderWidthPx).coerceIn(0f, 1f))
                        },
                        onDrag = { change, _ ->
                            onValueChange((change.position.x / sliderWidthPx).coerceIn(0f, 1f))
                        },
                        onDragEnd = onValueChangeFinished,
                        onDragCancel = onValueChangeFinished
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Gray200)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(coercedValue)
                    .height(4.dp)
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
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = White
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Primary100)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = HaloType.body02Medium,
                color = if (selected) Primary600 else Gray700,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
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
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
