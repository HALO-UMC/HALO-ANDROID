package com.umc.halo.presentation.themebox.show_theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.theme.Black
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ShowThemeRoute(
    vm: ShowThemeViewModel = hiltViewModel(),
    onNavigateBack: (Long?) -> Unit
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

                ShowThemeUiEvent.OnClickBackArrow -> onNavigateBack(state.storybookId)
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

    val currentIsPlaying by rememberUpdatedState(state.isPlaying)

    LaunchedEffect(state.currentPage) {
        var progress = 0f

        while (progress < 1f) {
            if (currentIsPlaying) {
                progress += 0.01f
                onEvent(ShowThemeUiEvent.UpdateProgress(progress))
            }
            delay(50)
        }

        if (state.currentPage == 10) {
            onEvent(ShowThemeUiEvent.OnClickBackArrow)
        } else {
            onEvent(ShowThemeUiEvent.NextPage)
        }
    }

    val currentPage = state.chapters.getOrNull(state.currentPage) ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()

                    onEvent(ShowThemeUiEvent.StopPage)

                    val up = waitForUpOrCancellation()

                    if (up != null) {
                        val duration = up.uptimeMillis - down.uptimeMillis

                        if (duration < 200) {
                            // 짧은 탭
                            if (down.position.x > size.width / 2f) {
                                onEvent(ShowThemeUiEvent.NextPage)
                            } else {
                                onEvent(ShowThemeUiEvent.PreviousPage)
                            }
                        }

                        // 짧은 탭이든 길게 누르든 손을 떼면 재생
                        onEvent(ShowThemeUiEvent.ResumePage)
                    }
                }
            }
    ) {
        // 배경 이미지
        AsyncImage(
            model = currentPage.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .background(Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ShowThemeTopBar(title = currentPage.title, onEvent = onEvent)

            StoryProgressBar(state)
        }

        // 하단 텍스트 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            Text(
                text = "${currentPage.id}장",
                style = HaloType.body02Medium,
                color = Gray100
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = currentPage.title,
                style = HaloType.heading01Bold,
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

            Spacer(Modifier.height(24.dp))
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
            .padding(23.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFD9D9D9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(state.progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
            )
        }
    }
}

