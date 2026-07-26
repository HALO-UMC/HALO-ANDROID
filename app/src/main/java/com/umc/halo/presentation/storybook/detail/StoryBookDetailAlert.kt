package com.umc.halo.presentation.storybook.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun StoryBookDetailAlert(
    onEvent: (StoryBookDetailUiEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(StoryBookDetailUiEvent.OnClickDismissDialog) },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "아직 열어볼 수 없어요",
                style = HaloType.body01SemiBold,
                color = Gray800,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "내일이 되면 새로운 장면을 열어볼 수 있어요.\n조금만 기다려 주세요 :)",
                    style = HaloType.body03Regular,
                    color = Gray800,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        },
        confirmButton = {
            HaloMaterialButton(
                buttonState = ButtonState.ABLE,
                text = "확인",
                modifier = Modifier
                    .fillMaxWidth(),
                style = HaloType.body02SemiBold
            ) {
                onEvent(StoryBookDetailUiEvent.OnClickDismissDialog)
            }
        }
    )
}