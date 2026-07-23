package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val ChapterButtonDisabledContainer = Color(0xFFEEEEEE)
private val ChapterButtonDisabledContent = Color(0xFF8C8C8C)

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
            disabledContainerColor = ChapterButtonDisabledContainer,
            disabledContentColor = ChapterButtonDisabledContent
        )
    ) {
        Text(
            text = text,
            style = HaloType.body01SemiBold
        )
    }
}

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