package com.umc.halo.presentation.themebox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500

@Composable
fun ThemeBoxFilledScreen(
    state: ThemeBoxUiState.Filled,
    initialStorybookId: Long? = null,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(12f))
        //Spacer(Modifier.height(24.dp))

        ProgressBox(
            Modifier.weight(35f),
            state.numberOfCharacter,
            state.storyBookInProgress
        )

        Spacer(Modifier.weight(18f))
        //Spacer(Modifier.height(36.dp))

        ThemeBox(
            Modifier.weight(190f),
            state.themeList,
            initialStorybookId,
            onEvent
        )

        Spacer(Modifier.weight(15f))
        //Spacer(Modifier.height(30.dp))

        HaloMaterialButton(
            buttonState = ButtonState.ABLE,
            text = "감상 시작",
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 24.dp),
            style = HaloType.body01SemiBold
        ) {
            if (state.currentStorybookId != null) {
                onEvent(ThemeBoxUiEvent.OnShowThemeClicked(state.currentStorybookId))
            }
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
    themeList: List<Theme>,
    initialStorybookId: Long? = null,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
    ) {
        CarouselPager(themeList, initialStorybookId, onEvent)
    }
}