package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterImagePlaceholder
import com.umc.halo.presentation.storybook.chapter.component.ChapterSpeechBubble

private val GuideCheckerLight = Color(0xFFFFF7DD)
private val GuideCheckerDark = Color(0xFFFFF0C8)

@Composable
fun ChapterGuideStep(
    chapter: Chapter,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        /*
         * 추후 guideImageUrl이 내려오면 이 영역 전체에
         * 배경과 캐릭터가 합쳐진 이미지를 표시합니다.
         */
        ChapterImagePlaceholder(
            imageUrl = chapter.guideImageUrl,
            showLabel = false,
            lightColor = GuideCheckerLight,
            darkColor = GuideCheckerDark,
            modifier = Modifier.fillMaxSize()
        )

        HaloTopBar(
            title = chapter.storybookTitle,
            showLeftIcon = true,
            onClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(
                    start = 32.dp,
                    top = 214.dp,
                    end = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChapterSpeechBubble(
                text = chapter.chapterGuideText
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
