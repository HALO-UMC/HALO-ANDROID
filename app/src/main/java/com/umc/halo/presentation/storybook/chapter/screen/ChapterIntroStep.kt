package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterImagePlaceholder
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val ChapterTagBackground = Color(0xFFFFF3E8)

@Composable
fun ChapterIntroStep(
    chapter: Chapter,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        val imageHeight = maxHeight * 0.56f
        val contentHeight = maxHeight - imageHeight

        ChapterImagePlaceholder(
            imageUrl = chapter.backgroundImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        HaloTopBar(
            title = chapter.storybookTitle,
            showLeftIcon = true,
            onClick = onBackClick
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            ),
            color = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        top = 24.dp,
                        end = 24.dp,
                        bottom = 100.dp
                    )
            ) {
                ChapterNumberTag(
                    chapterNumber = chapter.number
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = chapter.title,
                    style = HaloType.body01SemiBold.copy(
                        fontSize = 20.sp,
                        lineHeight = 30.sp
                    ),
                    color = Gray800
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = chapter.description,
                    style = HaloType.body02Regular,
                    color = Gray600
                )
            }
        }

        ChapterBottomAction(
            text = "다음",
            enabled = true,
            onClick = onNextClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ChapterNumberTag(
    chapterNumber: Int
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = ChapterTagBackground
    ) {
        Text(
            text = "${chapterNumber.toString().padStart(2, '0')}장",
            style = HaloType.caption01Medium,
            color = Primary500,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 8.dp
            )
        )
    }
}