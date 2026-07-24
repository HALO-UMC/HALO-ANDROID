package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.theme.Primary500

@Composable
fun ChapterSceneCardImage(
    card: ChapterSceneCard,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    cornerRadius: Dp = 10.dp
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .background(
                color = sceneCardBackgroundColor(card.id),
                shape = shape
            )
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = if (selected) Primary500 else Color.Transparent,
                shape = shape
            )
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            when (card.id) {
                1L -> drawLetterCard()
                2L -> drawTravelCard()
                3L -> drawTalkCard()
                else -> drawGraduationCard()
            }
        }
    }
}

private fun sceneCardBackgroundColor(
    cardId: Long
): Color {
    return when (cardId) {
        1L -> Color(0xFFFFF1D9)
        2L -> Color(0xFFFFD7A6)
        3L -> Color(0xFFDDEEFF)
        else -> Color(0xFFE8F7D5)
    }
}

private fun DrawScope.drawLetterCard() {
    drawRoundRect(
        color = Color(0xFFFFFFFF),
        topLeft = Offset(
            x = size.width * 0.22f,
            y = size.height * 0.22f
        ),
        size = androidx.compose.ui.geometry.Size(
            width = size.width * 0.56f,
            height = size.height * 0.46f
        ),
        cornerRadius = CornerRadius(8.dp.toPx())
    )

    drawLine(
        color = Color(0xFFD9C3A5),
        start = Offset(size.width * 0.3f, size.height * 0.38f),
        end = Offset(size.width * 0.7f, size.height * 0.38f),
        strokeWidth = 2.dp.toPx()
    )

    drawLine(
        color = Color(0xFFD9C3A5),
        start = Offset(size.width * 0.3f, size.height * 0.48f),
        end = Offset(size.width * 0.62f, size.height * 0.48f),
        strokeWidth = 2.dp.toPx()
    )

    drawCircle(
        color = Color(0xFFFF7B10),
        radius = size.minDimension * 0.055f,
        center = Offset(size.width * 0.5f, size.height * 0.7f)
    )
}

private fun DrawScope.drawTravelCard() {
    drawCircle(
        color = Color(0xFFFFF1D9),
        radius = size.minDimension * 0.22f,
        center = Offset(size.width * 0.72f, size.height * 0.25f)
    )

    val mountainPath = Path().apply {
        moveTo(size.width * 0.1f, size.height * 0.78f)
        lineTo(size.width * 0.35f, size.height * 0.42f)
        lineTo(size.width * 0.52f, size.height * 0.66f)
        lineTo(size.width * 0.72f, size.height * 0.36f)
        lineTo(size.width * 0.92f, size.height * 0.78f)
        close()
    }

    drawPath(
        path = mountainPath,
        color = Color(0xFFE6A86F)
    )

    drawRoundRect(
        color = Color(0xFFFFFFFF),
        topLeft = Offset(
            x = size.width * 0.18f,
            y = size.height * 0.62f
        ),
        size = androidx.compose.ui.geometry.Size(
            width = size.width * 0.48f,
            height = size.height * 0.16f
        ),
        cornerRadius = CornerRadius(20.dp.toPx())
    )
}

private fun DrawScope.drawTalkCard() {
    drawCircle(
        color = Color(0xFF404040),
        radius = size.minDimension * 0.12f,
        center = Offset(size.width * 0.34f, size.height * 0.38f)
    )

    drawCircle(
        color = Color(0xFF404040),
        radius = size.minDimension * 0.12f,
        center = Offset(size.width * 0.68f, size.height * 0.32f)
    )

    drawRoundRect(
        color = Color(0xFFFFFFFF),
        topLeft = Offset(size.width * 0.18f, size.height * 0.52f),
        size = androidx.compose.ui.geometry.Size(
            width = size.width * 0.58f,
            height = size.height * 0.18f
        ),
        cornerRadius = CornerRadius(24.dp.toPx())
    )
}

private fun DrawScope.drawGraduationCard() {
    drawCircle(
        color = Color(0xFFFFD6C9),
        radius = size.minDimension * 0.13f,
        center = Offset(size.width * 0.5f, size.height * 0.45f)
    )

    val hatPath = Path().apply {
        moveTo(size.width * 0.28f, size.height * 0.32f)
        lineTo(size.width * 0.5f, size.height * 0.2f)
        lineTo(size.width * 0.72f, size.height * 0.32f)
        lineTo(size.width * 0.5f, size.height * 0.44f)
        close()
    }

    drawPath(
        path = hatPath,
        color = Color(0xFF2B2B2B)
    )

    drawRoundRect(
        color = Color(0xFF2B2B2B),
        topLeft = Offset(size.width * 0.37f, size.height * 0.52f),
        size = androidx.compose.ui.geometry.Size(
            width = size.width * 0.26f,
            height = size.height * 0.3f
        ),
        cornerRadius = CornerRadius(10.dp.toPx())
    )

    drawCircle(
        color = Color(0xFFFF7B10),
        radius = size.minDimension * 0.035f,
        center = Offset(size.width * 0.76f, size.height * 0.76f)
    )
}