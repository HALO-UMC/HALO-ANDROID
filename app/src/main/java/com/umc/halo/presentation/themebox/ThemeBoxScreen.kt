package com.umc.halo.presentation.themebox

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import kotlin.math.absoluteValue

@Composable
fun ThemeBoxScreen() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        ProgressBox()
        
        Spacer(Modifier.height(26.dp))

        ThemeBox()
    }
}

@Composable
fun ProgressBox() {
    Row(
        Modifier
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
                style = HaloType.body03Medium,
                color = Gray600
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "3개",
                style = HaloType.body01SemiBold,
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
                style = HaloType.body03Medium,
                color = Gray600
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "3개",
                style = HaloType.body01SemiBold,
                color = Gray800
            )
        }
    }
}

@Composable
fun ThemeBox() {
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        CarouselPager()
    }
}


@Composable
fun CarouselPager() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val baseWidth = maxWidth
        val horizontalPadding = baseWidth * 0.2f
        val pageSpacing = baseWidth * 0.04f
        val pagerState = rememberPagerState(pageCount = { 10 })

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = pageSpacing
        ) { page ->
            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue

            val pagerSize = lerp(
                start = 0.78f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            val alpha = lerp(
                start = 0.78f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            Card(
                Modifier
                    .graphicsLayer(
                        scaleY = pagerSize,
                        alpha = alpha
                    )
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            ) {
                //내용 채우기
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeBoxPreview() {
    ThemeBoxScreen()
}
