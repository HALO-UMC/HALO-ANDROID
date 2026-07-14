package com.umc.halo.presentation.storybook.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType


private val CoverPlaceholderColor = Gray100 // TODO: 실제 커버 이미지로 추후 교체
@Composable
fun StoryBookIndex(
    item: StoryBookIndex
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .fillMaxWidth(9f / 26f)
                    .aspectRatio(54f / 29f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CoverPlaceholderColor)
            ) {
                //이미지 넣기
            }

            Spacer(Modifier.fillMaxWidth(11f/312f))

            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${item.id}장 ${item.title}",
                    style = HaloType.body03Medium
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = item.subTitle,
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            ) {
                //로티 애니매이션 넣기
            }
        }
    }

    Spacer(Modifier.height(12.dp))
}