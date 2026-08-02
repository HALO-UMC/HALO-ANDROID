package com.umc.halo.presentation.storybook.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import java.util.Locale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.TodayStoryBook
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
            text = if (todayStoryBook.isCompleted) "테마 감상하기" else if (todayStoryBook.isLocked) "내일 펼칠 장면" else "오늘 펼칠 장면",
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
            AsyncImage(
                model = todayStoryBook.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = if (todayStoryBook.isCompleted) "완성된 이야기를 감상해보세요" else "${todayStoryBook.id.toTwoDigits()}장 ${todayStoryBook.title}",
            style = HaloType.body02SemiBold,
            color = Gray800
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (todayStoryBook.isCompleted) "지금까지 기록한 열 개의 장면을 하나의 이야기로 만나볼 수 있어요." else todayStoryBook.tag,
            style = HaloType.caption01Regular,
            color = Gray600
        )

        Spacer(Modifier.height(24.dp))

        if (todayStoryBook.isCompleted) {
            HaloMaterialButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(46f/7f),
                text = "감상 시작",
                onClick = {
                    onEvent(StoryBookDetailUiEvent.OnClickTodayStoryBook(storyBookId,todayStoryBook.id))
                },
                buttonState = ButtonState.ABLE
            )
        } else if (todayStoryBook.isLocked) {
            HaloMaterialButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(46f/7f),
                text = "내일 만나요!",
                onClick = {
                    onEvent(StoryBookDetailUiEvent.OnClickOpenDialog)
                },
                buttonState = ButtonState.LINE
            )
        } else {
            HaloMaterialButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(46f/7f),
                text = "바로 시작하기",
                onClick = {
                    onEvent(StoryBookDetailUiEvent.OnClickTodayStoryBook(storyBookId,todayStoryBook.id))
                },
                buttonState = ButtonState.ABLE
            )
        }

    }
}

fun Long.toTwoDigits(): String {
    return String.format(Locale.US,"%02d", this)
}
