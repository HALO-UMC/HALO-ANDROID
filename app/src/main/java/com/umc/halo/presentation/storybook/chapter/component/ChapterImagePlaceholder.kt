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
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType
import kotlin.math.ceil

private val CheckerColorLight = Color(0xFFF7F7F7)
private val CheckerColorDark = Color(0xFFEDEDED)

/**
 * 서버 이미지가 연결되기 전 사용하는 이미지 Placeholder
 *
 * 추후 실제 이미지 표시 기능을 연결하면,
 * imageUrl이 없을 때만 이 Placeholder를 보여줍니다.
 */
@Composable
fun ChapterImagePlaceholder(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(CheckerColorLight),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val cellSize = 24.dp.toPx()
            val columnCount = ceil(size.width / cellSize).toInt()
            val rowCount = ceil(size.height / cellSize).toInt()

            repeat(rowCount) { row ->
                repeat(columnCount) { column ->
                    val color = if ((row + column) % 2 == 0) {
                        CheckerColorLight
                    } else {
                        CheckerColorDark
                    }

                    drawRect(
                        color = color,
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

        Text(
            text = if (imageUrl.isNullOrBlank()) {
                "챕터 배경 이미지"
            } else {
                "이미지 URL 연결 예정"
            },
            style = HaloType.body03Regular,
            color = Gray500
        )
    }
}