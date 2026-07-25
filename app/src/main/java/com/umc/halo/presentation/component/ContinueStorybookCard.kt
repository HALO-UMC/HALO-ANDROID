package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White
import com.umc.halo.presentation.themebox.ContinueStorybook

@Composable
fun ContinueStorybookCard(
    item: ContinueStorybook,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .aspectRatio(312f/76f)
            .dropShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 2.dp,
                    color = Color(0xCFECE9E7),
                    offset = DpOffset(0.dp, 0.dp)
                )
            )
            .clickable{
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Row(
            Modifier
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "오늘 ${item.currentProgress + 1}장까지 완료할 수 있어요!",
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Card(
                Modifier
                    .width(69.dp)
                    .height(36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Primary30
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.currentProgress}장",
                        style = HaloType.body02Medium,
                        color = Primary500
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(R.drawable.ic_continuestorybook_right_arrow),
                        contentDescription = null,
                        tint = Primary500
                    )
                }
            }
        }

    }
}