package com.umc.halo.presentation.storybook.chapter.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.ChapterSceneRecordMethod
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterSceneCardModal
import com.umc.halo.presentation.storybook.chapter.component.ChapterStepProgressBar
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val TagBackgroundColor = Color(0xFFFFFAF7)
private val TagTextColor = Color(0xFFFF9742)
private val SceneTitleColor = Color(0xFF0D0D0D)

private val OptionDefaultBackground = Color(0xFFF7F7F7)
private val OptionSelectedBackground = Color(0xFFFFF3E8)
private val OptionDefaultTextColor = Color(0xFF404040)

@Composable
fun ChapterSceneStep(
    chapter: Chapter,
    sceneCards: List<ChapterSceneCard>,
    selectedMethod: ChapterSceneRecordMethod?,
    pendingSceneCardId: Long?,
    isSceneCardModalVisible: Boolean,
    isSceneCardConfirmEnabled: Boolean,
    isNextEnabled: Boolean,
    onMethodSelected: (ChapterSceneRecordMethod) -> Unit,
    onImageSelected: (String) -> Unit,
    onSceneCardModalRequested: () -> Unit,
    onSceneCardModalDismissed: () -> Unit,
    onPendingSceneCardSelected: (Long) -> Unit,
    onSceneCardConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onImageSelected(uri.toString())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HaloTopBar(
                title = chapter.storybookTitle,
                showLeftIcon = true,
                onClick = onBackClick
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 110.dp)
                ) {
                    Spacer(modifier = Modifier.height(18.dp))

                    ChapterStepProgressBar(
                        currentStepIndex = 1,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    ChapterSceneHeader(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

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

                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        )

                        SceneRecordOptionButton(
                            text = "장면카드로 남기기",
                            method = ChapterSceneRecordMethod.SCENE_CARD,
                            selected = selectedMethod == ChapterSceneRecordMethod.SCENE_CARD,
                            onClick = {
                                onSceneCardModalRequested()
                            }
                        )
                    }
                }

                ChapterBottomAction(
                    text = "다음",
                    enabled = isNextEnabled,
                    onClick = onNextClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        if (isSceneCardModalVisible) {
            ChapterSceneCardModal(
                themeTitle = chapter.title,
                characterImageUrl = chapter.characterImageUrl,
                sceneCards = sceneCards,
                selectedCardId = pendingSceneCardId,
                isConfirmEnabled = isSceneCardConfirmEnabled,
                onCardClick = onPendingSceneCardSelected,
                onDismiss = onSceneCardModalDismissed,
                onConfirmClick = onSceneCardConfirmClick
            )
        }
    }
}

@Composable
private fun ChapterSceneHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = TagBackgroundColor,
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = "02",
                style = HaloType.body03Medium,
                color = TagTextColor,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "오늘의 장면을 남겨볼까요?",
            style = HaloType.body01SemiBold.copy(
                fontSize = 18.sp,
                lineHeight = 26.sp
            ),
            color = SceneTitleColor
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "사진으로 남겨도, 장면카드로 대신해도 좋아요.\n이 페이지에 어울리는 순간을 골라주세요.",
            style = HaloType.body03Regular,
            color = Gray400
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
    val contentColor = if (selected) {
        Primary500
    } else {
        OptionDefaultTextColor
    }

    val backgroundColor = if (selected) {
        OptionSelectedBackground
    } else {
        OptionDefaultBackground
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(
                width = 56.dp,
                height = 52.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            when (method) {
                ChapterSceneRecordMethod.PHOTO -> {
                    CameraIcon(
                        color = contentColor,
                        modifier = Modifier.size(
                            width = 34.dp,
                            height = 30.dp
                        )
                    )
                }

                ChapterSceneRecordMethod.SCENE_CARD -> {
                    SceneCardIcon(
                        color = contentColor,
                        modifier = Modifier.size(
                            width = 40.dp,
                            height = 30.dp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = HaloType.body02Regular,
            color = contentColor
        )
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
            topLeft = Offset(
                x = size.width * 0.08f,
                y = size.height * 0.28f
            ),
            size = Size(
                width = size.width * 0.84f,
                height = size.height * 0.58f
            ),
            cornerRadius = CornerRadius(
                x = 5.dp.toPx(),
                y = 5.dp.toPx()
            ),
            style = Stroke(width = strokeWidth)
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = size.width * 0.32f,
                y = size.height * 0.12f
            ),
            size = Size(
                width = size.width * 0.36f,
                height = size.height * 0.22f
            ),
            cornerRadius = CornerRadius(
                x = 4.dp.toPx(),
                y = 4.dp.toPx()
            ),
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = color,
            radius = size.minDimension * 0.18f,
            center = Offset(
                x = size.width / 2f,
                y = size.height * 0.58f
            ),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun SceneCardIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 3.dp.toPx()

        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = size.width * 0.05f,
                y = size.height * 0.12f
            ),
            size = Size(
                width = size.width * 0.9f,
                height = size.height * 0.76f
            ),
            cornerRadius = CornerRadius(
                x = 5.dp.toPx(),
                y = 5.dp.toPx()
            ),
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(
                x = size.width * 0.3f,
                y = size.height * 0.35f
            )
        )

        val path = Path().apply {
            moveTo(size.width * 0.14f, size.height * 0.78f)
            lineTo(size.width * 0.38f, size.height * 0.55f)
            lineTo(size.width * 0.55f, size.height * 0.68f)
            lineTo(size.width * 0.72f, size.height * 0.48f)
            lineTo(size.width * 0.88f, size.height * 0.78f)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth)
        )
    }
}
