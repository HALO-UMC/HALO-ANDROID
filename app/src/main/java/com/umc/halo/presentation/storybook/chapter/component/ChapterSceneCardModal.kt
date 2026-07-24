package com.umc.halo.presentation.storybook.chapter.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val DimColor = Color(0xCC000000)
private val DisabledButtonColor = Color(0xFFEEEEEE)

@Composable
fun ChapterSceneCardModal(
    themeTitle: String,
    sceneCards: List<ChapterSceneCard>,
    selectedCardId: Long?,
    isConfirmEnabled: Boolean,
    onCardClick: (Long) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    BackHandler {
        onDismiss()
    }

    Box(
        modifier = Modifier
            .background(DimColor)
            .clickable { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(288.dp)
                .height(344.dp)
                .clickable(enabled = false) {}
        ) {
            ChapterModalCharacter(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 0.dp)
                    .zIndex(1f)
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(288.dp)
                    .height(273.dp),
                shape = RoundedCornerShape(20.dp),
                color = White
            ) {
                Column(
                    modifier = Modifier
                        .width(288.dp)
                        .height(273.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = themeTitle,
                        style = HaloType.body01SemiBold,
                        color = Gray800,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "오늘의 이야기와 어울리는 장면카드를 골라주세요.",
                        style = HaloType.caption01Regular,
                        color = Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SceneCardGrid(
                        sceneCards = sceneCards,
                        selectedCardId = selectedCardId,
                        onCardClick = onCardClick
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SceneCardConfirmButton(
                        enabled = isConfirmEnabled,
                        onClick = onConfirmClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SceneCardGrid(
    sceneCards: List<ChapterSceneCard>,
    selectedCardId: Long?,
    onCardClick: (Long) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sceneCards
            .take(4)
            .chunked(2)
            .forEach { rowCards ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowCards.forEach { card ->
                        ChapterSceneCardImage(
                            card = card,
                            selected = selectedCardId == card.id,
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    onCardClick(card.id)
                                }
                        )
                    }
                }
            }
    }
}

@Composable
private fun SceneCardConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (enabled) {
        Primary500
    } else {
        DisabledButtonColor
    }

    val textColor = if (enabled) {
        White
    } else {
        Gray400
    }

    Box(
        modifier = Modifier
            .width(88.dp)
            .height(36.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "완료",
            style = HaloType.body03Medium,
            color = textColor
        )
    }
}

@Composable
private fun ChapterModalCharacter(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.size(
            width = 108.dp,
            height = 90.dp
        )
    ) {
        val bodyColor = Color(0xFFD7B77B)

        drawCircle(
            color = bodyColor,
            radius = 22.dp.toPx(),
            center = Offset(size.width * 0.5f, size.height * 0.45f)
        )

        drawCircle(
            color = bodyColor,
            radius = 16.dp.toPx(),
            center = Offset(size.width * 0.32f, size.height * 0.5f)
        )

        drawCircle(
            color = bodyColor,
            radius = 16.dp.toPx(),
            center = Offset(size.width * 0.68f, size.height * 0.5f)
        )

        drawCircle(
            color = bodyColor,
            radius = 15.dp.toPx(),
            center = Offset(size.width * 0.5f, size.height * 0.24f)
        )

        drawCircle(
            color = bodyColor,
            radius = 12.dp.toPx(),
            center = Offset(size.width * 0.22f, size.height * 0.68f)
        )

        drawCircle(
            color = bodyColor,
            radius = 12.dp.toPx(),
            center = Offset(size.width * 0.78f, size.height * 0.68f)
        )

        drawCircle(
            color = White,
            radius = 8.dp.toPx(),
            center = Offset(size.width * 0.43f, size.height * 0.53f)
        )

        drawCircle(
            color = White,
            radius = 8.dp.toPx(),
            center = Offset(size.width * 0.57f, size.height * 0.53f)
        )

        drawCircle(
            color = Color(0xFF0D0D0D),
            radius = 3.dp.toPx(),
            center = Offset(size.width * 0.45f, size.height * 0.55f)
        )

        drawCircle(
            color = Color(0xFF0D0D0D),
            radius = 3.dp.toPx(),
            center = Offset(size.width * 0.55f, size.height * 0.55f)
        )

        drawRoundRect(
            color = Primary500,
            topLeft = Offset(size.width * 0.46f, size.height * 0.61f),
            size = androidx.compose.ui.geometry.Size(
                width = size.width * 0.08f,
                height = size.height * 0.08f
            ),
            cornerRadius = CornerRadius(10.dp.toPx())
        )
    }
}