package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.HaloType

@Composable
fun ChapterBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HaloMaterialButton(
        buttonState = if (enabled) {
            ButtonState.ABLE
        } else {
            ButtonState.DISABLED
        },
        text = text,
        style = HaloType.body01SemiBold,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    )
}

/**
 * 모든 챕터 진행 화면에서 버튼 위치를 동일하게 유지하는 영역
 */
@Composable
fun ChapterBottomAction(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 16.dp
            )
    ) {
        ChapterBottomButton(
            text = text,
            enabled = enabled,
            onClick = onClick
        )
    }
}