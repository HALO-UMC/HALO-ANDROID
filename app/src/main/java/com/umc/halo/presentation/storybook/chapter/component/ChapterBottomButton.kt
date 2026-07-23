package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.HaloType

/**
 * 챕터 작성 플로우에서 공통으로 사용하는 하단 버튼
 *
 * 피그마 기준:
 * - 높이: 54dp
 * - 둥근 모서리: HaloMaterialButton의 30dp 사용
 * - 활성 색상: Primary500
 */
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