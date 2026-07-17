package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500

@Composable
fun CustomStorybookCard(
    item: CustomStorybook,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(246.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 커버 이미지 자리(현재는 임시) — 추후 구현 예정
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Gray100)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.tag,
                    style = HaloType.caption01Medium,
                    color = Primary500
                )
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )
                Text(
                    text = item.subtitle,
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_home_right_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}