package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType
import kotlin.math.ceil

private val DefaultCheckerColorLight = Color(0xFFF7F7F7)
private val DefaultCheckerColorDark = Color(0xFFEDEDED)

@Composable
fun ChapterImagePlaceholder(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    lightColor: Color = DefaultCheckerColorLight,
    darkColor: Color = DefaultCheckerColorDark
) {
    Box(
        modifier = modifier.background(lightColor),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val cellSize = 24.dp.toPx()
                val columnCount = ceil(size.width / cellSize).toInt()
                val rowCount = ceil(size.height / cellSize).toInt()

                repeat(rowCount) { row ->
                    repeat(columnCount) { column ->
                        val checkerColor =
                            if ((row + column) % 2 == 0) {
                                lightColor
                            } else {
                                darkColor
                            }

                        drawRect(
                            color = checkerColor,
                            topLeft = Offset(
                                x = column * cellSize,
                                y = row * cellSize
                            ),
                            size = Size(
                                width = cellSize,
                                height = cellSize
                            )
                        )
                    }
                }
            }
        }

        if (showLabel && imageUrl.isNullOrBlank()) {
            Text(
                text = "챕터 배경 이미지",
                style = HaloType.body03Regular,
                color = Gray500
            )
        }
    }
}
