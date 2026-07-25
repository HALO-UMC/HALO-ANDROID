package com.umc.halo.presentation.storybook.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.home.HomeViewModel
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType


private val ScreenPaddingHorizontal = 24.dp
private val CoverPlaceholderColor = Gray100 // TODO: 실제 커버 이미지로 추후 교체

@Composable
fun StoryBookDetailRoute(
    viewModel: StoryBookDetailViewModel = hiltViewModel(),
    onNavigateToChapterProgress: (Long, Long) -> Unit,
    onNavigateToChapterResult: (Long, Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    StoryBookDetailScreen(
        state = state,
        onEvent = { event ->
            fun navigate(storyBookId: Long, chapterId: Long) {
                val isCompleted = state.storyBookIndex[chapterId.toInt()].isCompleted

                if (isCompleted) {
                    onNavigateToChapterResult(storyBookId, chapterId)
                } else {
                    onNavigateToChapterProgress(storyBookId, chapterId)
                }
            }

            when (event) {
                is StoryBookDetailUiEvent.OnClickStoryBookIndex ->
                    navigate(event.storyBookId, event.chapterId)

                is StoryBookDetailUiEvent.OnClickTodayStoryBook ->
                    navigate(event.storyBookId, event.chapterId)
            }
        }
    )
}
@Composable
fun StoryBookDetailTopBar(
    vm: StoryBookDetailViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    HaloTopBar(
        title = state.storyBookInfo.title,
        showLeftIcon = false
    )
}

@Composable
fun StoryBookDetailScreen(
    state: StoryBookDetailUiState,
    onEvent: (StoryBookDetailUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPaddingHorizontal)
    ) {
        item {
            StoryBookIndexIntro(
                state,
                onEvent = onEvent)

            Spacer(Modifier.height(56.dp))

            Text(
                text = "이야기의 차례",
                style = HaloType.body01SemiBold,
                color = Gray800
            )

            Spacer(Modifier.height(18.dp))
        }

        items(
            items = state.storyBookIndex,
            key = { item -> item.id }
        ) { item ->
            StoryBookIndex(
                state.storyBookId,
                item,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun StoryBookIndexIntro(
    state: StoryBookDetailUiState,
    onEvent: (StoryBookDetailUiEvent) -> Unit
) {
    Column() {
        Spacer(Modifier.height(20.dp))

        StoryThemeIntro(state.storyBookInfo)

        Spacer(Modifier.height(36.dp))

        StoryBookProgress(state.storyBookProgress)

        Spacer(Modifier.height(48.dp))

        TodayStoryBook(state.storyBookId,state.todayStoryBookInfo, onEvent)
    }
}

private val StoryThemeIntroPadding = 12.dp
@Composable
fun StoryThemeIntro(
    storyBookInfo: StoryBookInfo
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "스토리 테마 소개",
            style = HaloType.body01SemiBold,
            color = Gray800,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(StoryThemeIntroPadding))

        Box(
            modifier = Modifier
                .height(132.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(CoverPlaceholderColor)
                .align(Alignment.CenterHorizontally)
        ) {
            //이미지 추가
        }

        Spacer(Modifier.height(StoryThemeIntroPadding))

        Text(
            text = storyBookInfo.storyBookIntro,
            style = HaloType.body03Regular,
            color = Gray500,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}



