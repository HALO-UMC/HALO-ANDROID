package com.umc.halo.presentation.storybook.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType


private val CoverPlaceholderColor = Gray100 // TODO: 실제 커버 이미지로 추후 교체
@Composable
fun TodayStoryBook(
    storyBookId: Long,
    todayStoryBook: TodayStoryBook,
    onEvent: (StoryBookDetailUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Gray30)
            .padding(16.dp)
    ) {
        Text(
            text = "오늘 펼칠 장면",
            style = HaloType.body01SemiBold,
            color = Gray800
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(276f/165f)
                .clip(RoundedCornerShape(20.dp))
                .background(CoverPlaceholderColor)
        ) {
            //이미지 넣기
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "${todayStoryBook.id.toTwoDigits()}장 ${todayStoryBook.title}",
            style = HaloType.body02SemiBold,
            color = Gray800
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = todayStoryBook.tag,
            style = HaloType.caption01Regular,
            color = Gray600
        )

        Spacer(Modifier.height(24.dp))

        HaloMaterialButton(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(46f/7f),
            text = "바로 시작하기",
            onClick = {
                onEvent(StoryBookDetailUiEvent.OnClickTodayStoryBook(storyBookId,todayStoryBook.id.toLong()))
            },
            buttonState = ButtonState.ABLE
        )
    }
}

fun Long.toTwoDigits(): String {
    return String.format(Locale.US,"%02d", this)
}
