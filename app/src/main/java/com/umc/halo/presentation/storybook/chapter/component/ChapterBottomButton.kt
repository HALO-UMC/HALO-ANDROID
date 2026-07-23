package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

@Composable
fun ChapterBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = White,
            disabledContainerColor = Gray100,
            disabledContentColor = Gray400
        )
    ) {
        Text(
            text = text,
            style = HaloType.body01SemiBold
        )
    }
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
            .imePadding()
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