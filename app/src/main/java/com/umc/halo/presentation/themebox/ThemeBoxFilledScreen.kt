package com.umc.halo.presentation.themebox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun ThemeBoxFilledScreen(
    state: ThemeBoxUiState.Filled
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(6f))
        //Spacer(Modifier.height(12.dp))

        ProgressBox(
            Modifier.weight(22f),
            state.numberOfCharacter,
            state.storyBookInProgress)

        Spacer(Modifier.weight(13f))
        //Spacer(Modifier.height(26.dp))

        ThemeBox(
            Modifier.weight(190f),
            state.themeList
        )

        Spacer(Modifier.weight(30f))
        //Spacer(Modifier.height(60.dp))

        HaloMaterialButton(
            buttonState = ButtonState.ABLE,
            text = "감상 시작",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .aspectRatio(52f/9f),
            style = HaloType.body01SemiBold
        ) {
            //네비게이션
        }

        Spacer(Modifier.weight(13f))
        //Spacer(Modifier.height(27.dp))
    }
}

@Composable
fun ProgressBox(
    modifier: Modifier = Modifier,
    numberOfCharacter: Int,
    storyBookInProgress: Int
) {
    Row(
        modifier
            .padding(10.dp)
            .fillMaxWidth(14/20f)
            .height(IntrinsicSize.Min)
    ) {
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "수집 캐릭터",
                style = HaloType.body02Regular,
                color = Gray600
            )

            Spacer(Modifier.weight(2f))
            //Spacer(Modifier.height(4.dp))

            Text(
                text = "${numberOfCharacter}개",
                style = HaloType.heading03SemiBold, //Medium으로 바꿔야 함
                color = Gray800
            )
        }

        VerticalDivider(
            thickness = 1.dp,
            color = Gray100
        )

        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "진행중인 스토리북",
                style = HaloType.body02Regular,
                color = Gray600
            )

            Spacer(Modifier.weight(2f))
            //Spacer(Modifier.height(4.dp))

            Text(
                text = "${storyBookInProgress}개",
                style = HaloType.heading03SemiBold,
                color = Gray800
            )
        }
    }
}

@Composable
fun ThemeBox(
    modifier: Modifier = Modifier,
    themeList: List<Theme>
) {
    Box(
        modifier
            .fillMaxWidth()
    ) {
        CarouselPager(themeList)
    }
}