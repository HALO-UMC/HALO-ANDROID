package com.umc.halo.presentation.themebox

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.umc.halo.R
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import kotlin.math.absoluteValue


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CarouselPager(
    themeList: List<Theme>
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val middle = Int.MAX_VALUE / 2
        val initialPage = middle - (middle % themeList.size)
        val baseWidth = maxWidth
        val horizontalPadding = baseWidth * 0.2f
        val pageSpacing = baseWidth * 0.04f
        val pagerState = rememberPagerState(
            pageCount = { Int.MAX_VALUE },
            initialPage = initialPage
        )

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = pageSpacing
        ) { page ->
            val item = themeList[page % themeList.size]

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
                        .aspectRatio(0.7f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.image_themebox_character_1),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.weight(9f))
                //Spacer(Modifier.height(18.dp))

                Text(
                    text = item.title,
                    style = HaloType.heading01SemiBold,
                    color = Gray800,
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = pagerSize,
                            scaleY = pagerSize,
                            alpha = alpha
                        )
                )

                Spacer(Modifier.weight(1f))
                //Spacer(Modifier.height(2.dp))

                Text(
                    text = item.subTitle,
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
