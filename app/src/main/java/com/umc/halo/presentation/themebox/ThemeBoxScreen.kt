package com.umc.halo.presentation.themebox

import android.annotation.SuppressLint
import androidx.appcompat.widget.DialogTitle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import kotlin.math.absoluteValue

data class Theme(
    val character: String,
    val title: String,
    val subTitle: String
)

val themeList = listOf(
    Theme("할로로","오래전 당신","가족과의 만남"),
    Theme("케로로","당신 사용 설명서", "부제"),
    Theme("기로로","가족의 온도", "부제"),
    Theme("도로로","취향이 닿는 날", "부제")
)

@Composable
fun ThemeBoxScreen() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        ProgressBox()
        
        Spacer(Modifier.height(26.dp))

        ThemeBox(themeList)

        Spacer(Modifier.height(60.dp))

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

        Spacer(Modifier.height(27.dp))
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
fun ThemeBox(
    themeList: List<Theme>
) {
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        CarouselPager(themeList)
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CarouselPager(
    themeList: List<Theme>
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val carouselItems = buildList {
            add(themeList.last())
            addAll(themeList)
            add(themeList.first())
        }

        val baseWidth = maxWidth
        val horizontalPadding = baseWidth * 0.2f
        val pageSpacing = baseWidth * 0.04f
        val pagerState = rememberPagerState(
            pageCount = { carouselItems.size },
            initialPage = 1
        )

        LaunchedEffect(pagerState.currentPage) {
            when (pagerState.currentPage) {
                0 -> pagerState.scrollToPage(themeList.size)
                carouselItems.lastIndex -> pagerState.scrollToPage(1)
            }
        }

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    Modifier
                        .graphicsLayer(
                            scaleY = pagerSize,
                            alpha = alpha
                        )
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                ) {
                    Text(carouselItems[page].character)
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = carouselItems[page].title,
                    style = HaloType.heading01SemiBold,
                    color = Gray800,
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = pagerSize,
                            scaleY = pagerSize,
                            alpha = alpha
                        )
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = carouselItems[page].subTitle,
                    style = HaloType.body02Medium,
                    color = Gray500,
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = pagerSize,
                            scaleY = pagerSize,
                            alpha = alpha
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeBoxPreview() {
    ThemeBoxScreen()
}
