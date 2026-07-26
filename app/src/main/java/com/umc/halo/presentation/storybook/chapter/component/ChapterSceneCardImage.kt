package com.umc.halo.presentation.storybook.chapter.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

private val SceneCardPlaceholderColor = Color(0xFFF7F7F7)
private val SceneCardPlaceholderLineColor = Color(0xFFE6E6E6)
private val SceneCardSelectedOverlay = Color(0x33000000)

@Composable
fun ChapterSceneCardImage(
    card: ChapterSceneCard,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    cornerRadius: Dp = 10.dp
) {
    val shape = RoundedCornerShape(cornerRadius)
    var imageBitmap by remember(card.imageUrl) {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(card.imageUrl) {
        imageBitmap = if (card.imageUrl.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    URL(card.imageUrl).openStream().use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                color = SceneCardPlaceholderColor,
                shape = shape
            )
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = shape
            )
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = card.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val cellSize = 10.dp.toPx()
                val strokeWidth = 1.dp.toPx()

                var x = cellSize
                while (x < size.width) {
                    drawLine(
                        color = SceneCardPlaceholderLineColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth
                    )
                    x += cellSize
                }

                var y = cellSize
                while (y < size.height) {
                    drawLine(
                        color = SceneCardPlaceholderLineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                    y += cellSize
                }
            }
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SceneCardSelectedOverlay)
            )
        }
    }
}
