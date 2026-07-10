package com.umc.halo.presentation.home.actionguide

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary50

@Composable
fun ActionGuide() {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "할로 행동 가이드",
            style = HaloType.body01SemiBold,
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "할로가 처음이라면!",
            style = HaloType.caption01Medium,
            color = Gray500
        )

        Spacer(Modifier.height(24.dp))

        ActionGuideFrame(
            number = "1",
            contents = "준비된 스토리북 테마를 골라요"
        )

        ActionGuideFrame(
            number = "2",
            contents = "준비된 스토리북 테마를 골라요"
        )

        ActionGuideFrame(
            number = "3",
            contents = "준비된 스토리북 테마를 골라요"
        )
    }
}

@Composable
fun ActionGuideFrame(
    number: String,
    contents: String
) {
    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .border(
                    width = 1.dp,
                    color = Primary50,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Primary30)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            width = 1.dp,
                            color = Gray30,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Text(
                        text = number,
                        style = HaloType.caption01Medium,
                        color = Gray400,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = contents,
                    style = HaloType.body03Regular,
                    color = Gray600,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_home_action_guide),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 22.dp)
                .background(Gray200)
        )

        Spacer(Modifier.height(4.dp))
    }
}