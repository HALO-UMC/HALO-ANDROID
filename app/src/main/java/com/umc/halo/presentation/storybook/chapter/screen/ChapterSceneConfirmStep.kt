package com.umc.halo.presentation.storybook.chapter.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.ChapterSceneRecordMethod
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterSceneCardImage
import com.umc.halo.presentation.storybook.chapter.component.rememberSelectedImageBitmap
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

private val ChangeButtonBackground = Color(0xFFF7F7F7)

@Composable
fun ChapterSceneConfirmStep(
    chapter: Chapter,
    selectedMethod: ChapterSceneRecordMethod?,
    selectedImageUri: String?,
    selectedSceneCard: ChapterSceneCard?,
    onImageSelected: (String) -> Unit,
    onSceneCardChangeClick: () -> Unit,
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 110.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(57.dp))

                Text(
                    text = "장면 선택이 완료되었어요!",
                    style = HaloType.body01SemiBold,
                    color = Color(0xFF0D0D0D),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "이미지 변경을 원할 시, 하단 버튼을 선택해주세요!",
                    style = HaloType.body03Regular,
                    color = Gray400,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(42.dp))

                SelectedScenePreview(
                    selectedMethod = selectedMethod,
                    selectedImageUri = selectedImageUri,
                    selectedSceneCard = selectedSceneCard
                )

                Spacer(modifier = Modifier.height(12.dp))

                SceneImageChangeButton(
                    text = "카드 이미지 변경하기",
                    onClick = {
                        when (selectedMethod) {
                            ChapterSceneRecordMethod.PHOTO -> {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }

                            ChapterSceneRecordMethod.SCENE_CARD -> {
                                onSceneCardChangeClick()
                            }

                            null -> {
                                onSceneCardChangeClick()
                            }
                        }
                    }
                )
            }

            ChapterBottomAction(
                text = "다음",
                enabled = true,
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun SelectedScenePreview(
    selectedMethod: ChapterSceneRecordMethod?,
    selectedImageUri: String?,
    selectedSceneCard: ChapterSceneCard?
) {
    when (selectedMethod) {
        ChapterSceneRecordMethod.PHOTO -> {
            SelectedPhotoImage(
                selectedImageUri = selectedImageUri
            )
        }

        ChapterSceneRecordMethod.SCENE_CARD -> {
            if (selectedSceneCard != null) {
                ChapterSceneCardImage(
                    card = selectedSceneCard,
                    cornerRadius = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 312.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                )
            } else {
                EmptySelectedSceneBox()
            }
        }

        null -> {
            EmptySelectedSceneBox()
        }
    }
}

@Composable
private fun SelectedPhotoImage(
    selectedImageUri: String?
) {
    val imageBitmap = rememberSelectedImageBitmap(selectedImageUri)

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "선택한 장면 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 312.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        EmptySelectedSceneBox()
    }
}

@Composable
private fun EmptySelectedSceneBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 312.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "선택한 이미지가 없어요",
            style = HaloType.body03Regular,
            color = Gray400
        )
    }
}

@Composable
private fun SceneImageChangeButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(ChangeButtonBackground)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = HaloType.caption01Medium,
            color = Gray400
        )
    }
}
