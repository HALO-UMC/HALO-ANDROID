package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White
import com.umc.halo.presentation.themebox.ContinueStorybook

@Composable
fun ContinueStorybookCard(
    item: ContinueStorybook
) {
    Card(
        Modifier
            .fillMaxWidth()
            .aspectRatio(156f/37f)
            .background(White)
            .dropShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 8.dp,
                    color = Color(0x1A858585),
                    offset = DpOffset(0.dp, 0.dp)
                )
            )
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp)
    ) {
        Row {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "오늘 ${item.chapter}장까지 완료할 수 있어요!",
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Card(
                Modifier
                    .width(69.dp)
                    .height(36.dp)
                    .background(Primary30)
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row{
                    Text(
                        text = "${item.chapter}장",
                        style = HaloType.body02Medium,
                        color = Primary500
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(R.drawable.ic_continuestorybook_right_arrow),
                        contentDescription = null
                    )
                }
            }
        }

    }
}