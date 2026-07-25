package com.umc.halo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.home.ProgressState
import com.umc.halo.domain.model.home.StartStorybook
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.presentation.theme.Black
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

@Composable
fun StartStorybook(
    item: StartStorybook,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "${item.storybookId}장을 바로 시작해보세요!",
            style = HaloType.body01SemiBold,
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(12.dp))

        Box() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(114.dp)
                    .dropShadow(
                        shape = RoundedCornerShape(12.dp),
                        shadow = Shadow(
                            radius = 4.dp,
                            spread = 2.dp,
                            color = Color(0xCFECE9E7),
                            offset = DpOffset(0.dp, 0.dp)
                        )
                    )
                    .clickable {
                        onEvent(
                            HomeUiEvent.OnStartStorybookClicked(
                                storyBookId = item.storybookId
                            )
                        )
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {
                StartStorybookContents(item)
            }

            if (item.isCompleted)
                ContentsOverlay(item)
        }
    }
}

@Composable
fun ContentsOverlay(
    item: StartStorybook
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(114.dp)
            .alpha(0.9f)
            .clickable { }, //contents click overlay
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Box(
            Modifier.fillMaxSize()
        )
        {
            Text(
                text = "테마 ${item.currentProgress + 1}장은\n'내일 다시' 참여할 수 있어요!",
                style = HaloType.body01Medium,
                color = Gray600,
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StartStorybookContents(
    item: StartStorybook
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            )
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(75.dp)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color(0xFFF6F6F6))
        ) {

        }

        Spacer(Modifier.weight(2f))

        Column(
            modifier = Modifier
                .width(125.dp)
                .fillMaxHeight()
        ) {
            Spacer(Modifier.weight(2.5f))

            Text(
                //--백엔드 전달 방식 고려 후 제작
                text = item.title,
                style = HaloType.body01SemiBold,
                color = Black
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "오늘 ${item.currentProgress + 1}장까지 완료할 수 있어요!",
                style = HaloType.caption01Regular,
                color = Gray500
            )

            if (item.currentProgress != 0) {
                Spacer(Modifier.weight(5f))

                Text(
                    text = "${item.currentProgress}/10",
                    style = HaloType.caption01Regular,
                    color = Primary500
                )

                Spacer(Modifier.weight(3f))

                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(121.dp)
                        .border(
                            width = 0.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .background(Gray100)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(((item.currentProgress/10f)*121).dp)
                            .border(
                                width = 0.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .background(Primary500)
                    )
                }
            }

            Spacer(Modifier.weight(4f))
        }

        Spacer(Modifier.weight(5f))

        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
