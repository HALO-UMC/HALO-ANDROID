package com.umc.halo.presentation.home

import android.util.Log
import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.component.ContinueStorybookCard
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.themebox.ContinueStorybook

@Composable
fun ContinueStorybookHome(
    item: List<ContinueStorybook>
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

        Spacer(Modifier.height(12.dp))

        val pagerState = rememberPagerState(
            pageCount = { (item.size + 1) / 2 }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 24.dp,
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) { page ->

            val firstIndex = page * 2
            val secondIndex = firstIndex + 1

            Column(
                Modifier.fillMaxWidth()

            ) {
                ContinueStorybookCard(item[firstIndex])

                if (secondIndex < item.size) {
                    Spacer(Modifier.height(10.dp))
                    ContinueStorybookCard(item[secondIndex])
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