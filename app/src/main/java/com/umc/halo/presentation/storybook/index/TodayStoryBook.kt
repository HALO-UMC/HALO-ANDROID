package com.umc.halo.presentation.storybook.index

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
fun TodayStoryBook() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Gray30)
            .padding(18.dp)
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
            text = "01장 나와 같은 나이였던 시절",
            style = HaloType.body02SemiBold,
            color = Gray800
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "지금의 내 나이였을 때 부모님은 어떤 하루를 살고 있었는지 들어봅시다.",
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

            },
            buttonState = ButtonState.ABLE
        )
    }
}
