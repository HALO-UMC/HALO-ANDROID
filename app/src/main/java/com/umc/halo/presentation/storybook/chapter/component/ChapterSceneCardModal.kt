package com.umc.halo.presentation.storybook.chapter.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val DimColor = Color(0xCC000000)
private val DisabledButtonColor = Color(0xFFEEEEEE)
private val CharacterPlaceholderColor = Color(0xFFF7F7F7)
private val CharacterPlaceholderLineColor = Color(0xFFD9D9D9)

@Composable
fun ChapterSceneCardModal(
    themeTitle: String,
    characterImageUrl: String?,
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
            .fillMaxSize()
            .background(DimColor)
            .clickable { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(288.dp)
                .height(358.dp)
                .clickable {}
        ) {
            ChapterModalCharacter(
                characterImageUrl = characterImageUrl,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 8.dp)
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
                        .fillMaxSize()
                        .padding(
                            start = 32.dp,
                            top = 24.dp,
                            end = 32.dp,
                            bottom = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = themeTitle,
                        style = HaloType.body01SemiBold,
                        color = Gray800,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "오늘의 이야기와 어울리는 장면카드를 골라주세요.",
                        style = HaloType.caption01Regular,
                        color = Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SceneCardGrid(
                        modifier = Modifier.fillMaxWidth(),
                        sceneCards = sceneCards,
                        selectedCardId = selectedCardId,
                        onCardClick = onCardClick
                    )

                    Spacer(modifier = Modifier.weight(1f))

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
    modifier: Modifier = Modifier,
    sceneCards: List<ChapterSceneCard>,
    selectedCardId: Long?,
    onCardClick: (Long) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sceneCards
            .take(2)
            .chunked(2)
            .forEach { rowCards ->
                Row(
                    modifier = Modifier.width(212.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 12.dp,
                        alignment = Alignment.CenterHorizontally
                    )
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
            .width(120.dp)
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
    characterImageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (!characterImageUrl.isNullOrBlank()) {
        AsyncImage(
            model = characterImageUrl,
            contentDescription = "장면 선택 캐릭터",
            modifier = modifier.size(
                width = 108.dp,
                height = 90.dp
            ),
            contentScale = ContentScale.Fit
        )
        return
    }

    Canvas(
        modifier = modifier
            .size(
                width = 108.dp,
                height = 90.dp
            )
            .background(
                color = CharacterPlaceholderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = CharacterPlaceholderLineColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        val cellSize = 9.dp.toPx()
        val strokeWidth = 1.dp.toPx()

        var x = cellSize
        while (x < size.width) {
            drawLine(
                color = CharacterPlaceholderLineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = strokeWidth
            )
            x += cellSize
        }

        var y = cellSize
        while (y < size.height) {
            drawLine(
                color = CharacterPlaceholderLineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
            y += cellSize
        }
    }
}
