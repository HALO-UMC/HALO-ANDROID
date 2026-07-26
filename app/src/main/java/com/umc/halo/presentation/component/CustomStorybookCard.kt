package com.umc.halo.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

/**
 * 맞춤 스토리북 카드
 *
 * @param coverRes 커버 이미지. null이면 회색 플레이스홀더
 */
@Composable
fun CustomStorybookCard(
    item: CustomStorybook,
    modifier: Modifier = Modifier,
    @DrawableRes coverRes: Int? = null,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = shape)   // 카드 그림자
            .clip(shape)
            .background(White)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커버 이미지 — 서버 연동 후 실제 이미지로 교체
        Box(
            modifier = Modifier
                .width(75.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gray50)
        ) {
            if (coverRes != null) {
                Image(
                    painter = painterResource(coverRes),
                    contentDescription = null,      // 장식용 이미지라 null
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = item.tag,
                    style = HaloType.body03Medium,
                    color = Primary500
                )
                Column {
                    Text(
                        text = item.title,
                        style = HaloType.body01SemiBold,
                        color = Gray800
                    )
                    Text(
                        text = item.subtitle,
                        style = HaloType.body03Regular,
                        color = Gray500
                    )
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_home_right_arrow),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
