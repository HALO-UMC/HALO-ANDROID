package com.umc.halo.presentation.themebox.show_theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White


@Composable
fun ShowThemeRoute(
    vm: ShowThemeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    ShowThemeScreen(
        state = state,
        onEvent = {
            onNavigateBack()
        }
    )
}

@Composable
fun ShowThemeTopBar(
    state: ShowThemeUiState,
    onEvent: (ShowThemeUiEvent) -> Unit
) {
    HaloTopBar(
        title = state.title,
        showLeftIcon = true,
        modifier = Modifier.background(Color.Transparent),
        titleColor = White
    ) {
        onEvent(ShowThemeUiEvent.OnClickBackArrow)
    }
}

@Composable
fun ShowThemeScreen(
    state: ShowThemeUiState,
    onEvent: (ShowThemeUiEvent) -> Unit
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        // 뒤쪽 이미지 자리 (비워둠)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .background(Color.Black) // 이미지 들어갈 자리
        )

        ShowThemeTopBar(state, onEvent)

        // 하단 텍스트 콘텐츠
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 24.dp)
        ) {
            Text(
                text = "${state.id}장",
                style = HaloType.body02Medium,
                color = Gray100
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = state.title,
                style = HaloType.heading01SemiBold, //bold로 바꿔야 함
                color = Gray100
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "최종 완료일 | ${state.completedDate}",
                style = HaloType.caption01Regular,
                color = Gray100
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.summary,
                style = HaloType.body02Medium,
                color = Gray100,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun ShowThemeScreenPreview() {
    ShowThemeScreen(
        state = ShowThemeUiState(
            id = 1,
            title = "나와 같은 나이였던 시절",
            completedDate = "2026.06.15",
            summary = "지금의 나와 같은 나이였을 때 부모님은 낯선 도시에서 첫 직장생활을 시작했다고 했다. " +
                    "익숙하지 않은 일과 빠듯한 생활 속에서도 주말마다 친구들과 시간을 보내는 것이 작은 즐거움이었다고 한다. " +
                    "지금의 내 나이에도 부모님만의 고민과 평범한 하루가 있었다는 사실이 오래 마음에 남았다."
        ),
        onEvent = {}
    )
}
