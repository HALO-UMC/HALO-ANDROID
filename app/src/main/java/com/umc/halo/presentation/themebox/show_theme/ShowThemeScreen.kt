package com.umc.halo.presentation.themebox.show_theme

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ShowThemeRoute(
    vm: ShowThemeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        vm.getThemeExhibition()
    }

    val state by vm.uiState.collectAsState()

    ShowThemeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                ShowThemeUiEvent.PreviousPage,ShowThemeUiEvent.NextPage,
                     ShowThemeUiEvent.ResumePage, ShowThemeUiEvent.StopPage, -> vm.onEvent(event)

                is ShowThemeUiEvent.UpdateProgress -> vm.onEvent(event)

                ShowThemeUiEvent.OnClickBackArrow -> onNavigateBack()
            }
        }
    )
}

@Composable
fun ShowThemeTopBar(
    title: String,
    onEvent: (ShowThemeUiEvent) -> Unit
) {
    HaloTopBar(
        title = title,
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
    val pagerState = rememberPagerState(
        pageCount = { state.chapters.size }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.currentPage, state.isPlaying) {
        while (state.progress < 1f) {
            delay(50)

            onEvent(
                ShowThemeUiEvent.UpdateProgress(
                    state.progress + 0.01f
                )
            )
        }

        onEvent(
            ShowThemeUiEvent.NextPage
        )

        pagerState.animateScrollToPage(
            state.currentPage
        )
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val currentPage = state.chapters[page]

        Column(
            Modifier.fillMaxSize()
        ) {
            StoryProgressBar(
                state = state
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            var longPress  = false

                            val longPressJob = scope.launch {
                                delay(500)
                                longPress = true
                                onEvent(ShowThemeUiEvent.StopPage)
                            }

                            val up = waitForUpOrCancellation()

                            longPressJob.cancel()

                            if (up != null) {
                                if (longPress) {
                                    onEvent(ShowThemeUiEvent.ResumePage)
                                } else {
                                    if (down.position.x > size.width / 2f) {
                                        onEvent(ShowThemeUiEvent.NextPage)
                                    } else {
                                        onEvent(ShowThemeUiEvent.PreviousPage)
                                    }
                                }
                            }
                        }
                    }
            ) {
                // 뒤쪽 이미지 자리 (비워둠)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = currentPage.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ShowThemeTopBar(currentPage.title, onEvent)

                // 하단 텍스트 콘텐츠
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = "${currentPage.id}장",
                        style = HaloType.body02Medium,
                        color = Gray100
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = currentPage.title,
                        style = HaloType.heading01SemiBold, //bold로 바꿔야 함
                        color = Gray100
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "최종 완료일 | ${currentPage.completedDate}",
                        style = HaloType.caption01Regular,
                        color = Gray100
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentPage.summary,
                        style = HaloType.body02Medium,
                        color = Gray100,
                    )
                }
            }
        }
    }

}

@Composable
fun StoryProgressBar(
    state: ShowThemeUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        state.chapters.forEachIndexed { index, _ ->

            LinearProgressIndicator(
                progress = {
                    when {
                        index < state.currentPage -> 1f
                        index == state.currentPage -> state.progress
                        else -> 0f
                    }
                },
                modifier = Modifier
                    .weight(1f) ,
                color = White,
                trackColor = Gray100
            )
        }
    }
}

