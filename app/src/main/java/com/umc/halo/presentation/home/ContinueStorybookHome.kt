package com.umc.halo.presentation.home

import android.util.Log
import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.home.StartStorybook
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.component.ContinueStorybookCard
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White
import com.umc.halo.presentation.themebox.ContinueStorybook

@Composable
fun ContinueStorybookHome(
    item: List<ContinueStorybook>,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = "진행중인 스토리북",
            style = HaloType.body01SemiBold,
            color = Color(0xFF3C3A35),
            modifier = Modifier
            .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        val pagerState = rememberPagerState(
            pageCount = { (item.size + 1) / 2 }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            pageSpacing = 24.dp,
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalAlignment = Alignment.Top //위쪽 정렬

        ) { page ->

            val firstItem = item[page * 2]

            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top //위쪽 정렬
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ContinueStorybookCard(firstItem) {
                        onEvent(HomeUiEvent.OnContinueStoryBookClicked(firstItem.storybookId))
                    }

                    if (!firstItem.todayAvailable) {
                        ContentsOverlay(firstItem, Modifier.matchParentSize())
                    }
                }

                if (page * 2 + 1 < item.size) {
                    val secondItem = item[page * 2 + 1]

                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ContinueStorybookCard(secondItem) {
                            onEvent(HomeUiEvent.OnContinueStoryBookClicked(secondItem.storybookId))
                        }

                        if (!secondItem.todayAvailable) {
                            ContentsOverlay(secondItem, Modifier.matchParentSize())
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        PageIndicator(pagerState)
    }
}

@Composable
fun PageIndicator(
    pagerState: PagerState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(pagerState.pageCount) { page ->
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (pagerState.currentPage == page) {
                            Gray300
                        } else {
                            Gray100
                        }
                    )
            )
        }
    }
}


@Composable
fun ContentsOverlay(
    item: ContinueStorybook,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
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
                text = "테마 ${item.currentChapterOrder}장은\n'내일 다시' 참여할 수 있어요!",
                style = HaloType.body01Medium,
                color = Gray600,
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}