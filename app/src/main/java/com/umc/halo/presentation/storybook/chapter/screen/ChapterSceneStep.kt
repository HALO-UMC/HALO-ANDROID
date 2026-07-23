package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.ChapterSceneRecordMethod
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val ProgressActiveColor = Color(0xFF6F6F6F)
private val ProgressInactiveColor = Color(0xFFEEEEEE)

private val TagBackgroundColor = Color(0xFFFFFAF7)
private val TagTextColor = Color(0xFFFF9742)

private val OptionDefaultBackground = Color(0xFFF7F7F7)
private val OptionSelectedBackground = Color(0xFFFFF3E8)
private val OptionTextDefaultColor = Color(0xFF404040)

@Composable
fun ChapterSceneStep(
    chapter: Chapter,
    selectedMethod: ChapterSceneRecordMethod?,
    isNextEnabled: Boolean,
    onMethodSelected: (ChapterSceneRecordMethod) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HaloTopBar(
            title = chapter.storybookTitle,
            showLeftIcon = true,
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(30.dp))

        ChapterStepProgressBar(
            selectedIndex = 1,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            ChapterStepTag(text = "02")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "오늘의 장면을 남겨볼까요?",
                style = HaloType.body01SemiBold,
                color = Gray800
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "사진으로 남겨도, 장면카드로 대신해도 좋아요.\n이 페이지에 어울리는 순간을 골라주세요.",
                style = HaloType.body03Regular,
                color = Color(0xFF8C8C8C)
            )
        }

        Spacer(modifier = Modifier.height(74.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SceneRecordOptionButton(
                text = "사진으로 남기기",
                method = ChapterSceneRecordMethod.PHOTO,
                selected = selectedMethod == ChapterSceneRecordMethod.PHOTO,
                onClick = {
                    onMethodSelected(ChapterSceneRecordMethod.PHOTO)
                }
            )

            SceneRecordOptionButton(
                text = "장면카드로 남기기",
                method = ChapterSceneRecordMethod.SCENE_CARD,
                selected = selectedMethod == ChapterSceneRecordMethod.SCENE_CARD,
                onClick = {
                    onMethodSelected(ChapterSceneRecordMethod.SCENE_CARD)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ChapterBottomAction(
            text = "다음",
            enabled = isNextEnabled,
            onClick = onNextClick
        )
    }
}

@Composable
private fun ChapterStepProgressBar(
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(
                        color = if (index == selectedIndex) {
                            ProgressActiveColor
                        } else {
                            ProgressInactiveColor
                        },
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
private fun ChapterStepTag(
    text: String
) {
    Box(
        modifier = Modifier
            .background(
                color = TagBackgroundColor,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = HaloType.caption01Medium,
            color = TagTextColor
        )
    }
}

@Composable
private fun SceneRecordOptionButton(
    text: String,
    method: ChapterSceneRecordMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) Primary500 else OptionTextDefaultColor
    val backgroundColor = if (selected) OptionSelectedBackground else OptionDefaultBackground
    val borderColor = if (selected) Primary500 else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SceneRecordOptionIcon(
            method = method,
            selected = selected
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = HaloType.body02Regular,
            color = contentColor
        )
    }
}

@Composable
private fun SceneRecordOptionIcon(
    method: ChapterSceneRecordMethod,
    selected: Boolean
) {
    val iconColor = if (selected) Primary500 else Color(0xFF404040)

    Box(
        modifier = Modifier.size(
            width = 56.dp,
            height = 52.dp
        ),
        contentAlignment = Alignment.Center
    ) {
        when (method) {
            ChapterSceneRecordMethod.PHOTO -> CameraIcon(
                color = iconColor,
                modifier = Modifier.size(
                    width = 34.dp,
                    height = 30.dp
                )
            )

            ChapterSceneRecordMethod.SCENE_CARD -> ImageIcon(
                color = iconColor,
                modifier = Modifier.size(
                    width = 40.dp,
                    height = 30.dp
                )
            )
        }
    }
}

@Composable
private fun CameraIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()

        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(
                x = size.width * 0.08f,
                y = size.height * 0.25f
            ),
            size = androidx.compose.ui.geometry.Size(
                width = size.width * 0.84f,
                height = size.height * 0.62f
            ),
            cornerRadius = CornerRadius(
                x = 5.dp.toPx(),
                y = 5.dp.toPx()
            ),
            style = Stroke(width = strokeWidth)
        )

        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(
                x = size.width * 0.34f,
                y = size.height * 0.08f
            ),
            size = androidx.compose.ui.geometry.Size(
                width = size.width * 0.32f,
                height = size.height * 0.2f
            ),
            cornerRadius = CornerRadius(
                x = 3.dp.toPx(),
                y = 3.dp.toPx()
            )
        )

        drawCircle(
            color = color,
            radius = size.minDimension * 0.17f,
            center = androidx.compose.ui.geometry.Offset(
                x = size.width / 2f,
                y = size.height * 0.57f
            ),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun ImageIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()

        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(
                x = 5.dp.toPx(),
                y = 5.dp.toPx()
            ),
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = color,
            radius = size.minDimension * 0.08f,
            center = androidx.compose.ui.geometry.Offset(
                x = size.width * 0.28f,
                y = size.height * 0.32f
            )
        )

        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width * 0.12f, size.height * 0.8f)
            lineTo(size.width * 0.38f, size.height * 0.52f)
            lineTo(size.width * 0.56f, size.height * 0.68f)
            lineTo(size.width * 0.72f, size.height * 0.48f)
            lineTo(size.width * 0.9f, size.height * 0.8f)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth)
        )
    }
}