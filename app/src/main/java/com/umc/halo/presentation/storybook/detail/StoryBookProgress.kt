package com.umc.halo.presentation.storybook.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.home.CurrentProgress
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500

@Composable
fun StoryBookProgress(
    storyBookProgress: CurrentProgress
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = progressString(storyBookProgress.chapter), // 상황에 따라 문구 변경 필요
            style = HaloType.body02Regular,
            color = Gray700
        )

        Spacer(Modifier.height(19.dp))

        StoryBookProgressBar(storyBookProgress)
    }
}

@Composable
fun StoryBookProgressBar(
    storyBookProgress: CurrentProgress
) {
    Column(
        modifier = Modifier
            .padding(start = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(
                    color = Gray50,
                    shape = RoundedCornerShape(24.dp))
        ) {
            ProgressDivider(10)

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(storyBookProgress.chapter/10f) // 나누기 n배 (임시: 3)
                    .border(
                        width = 0.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(
                        color = Primary500,
                        shape = RoundedCornerShape(24.dp)
                    )
            )
        }

        Spacer(Modifier.height(9.dp))
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (storyBookProgress.chapter <= 9) {
            Spacer(Modifier.fillMaxWidth(storyBookProgress.chapter/10f))

            ProgressIndicator(storyBookProgress, true)
        } else {
            Spacer(Modifier.weight(1f))

            ProgressIndicator(storyBookProgress, false)
        }
    }
}

@Composable
fun ProgressDivider(
    number: Int
) {
    Row() {
        Spacer(modifier = Modifier.weight(1f))

        repeat(number-1) {
            Icon(
                painter = painterResource(R.drawable.ic_storybookindex_progress_divider),
                contentDescription = null,
                tint = Gray100
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun ProgressIndicator(
    storyBookProgress: CurrentProgress,
    applyOffset: Boolean
) {
    Column(
        modifier = if (applyOffset) Modifier.offset(x = (-22).dp) else Modifier
    ) {
        if (applyOffset) {
            Canvas(
                modifier = Modifier
                    .width(12.dp)
                    .height(6.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(0f, size.height)
                    lineTo(size.width, size.height)
                    close()
                }

                drawPath(
                    path = path,
                    color = Primary500
                )
            }
        }
        
        Box(
            modifier = Modifier
                .height(27.dp)
                .width(44.dp)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = Primary500,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = "${storyBookProgress.chapter*10}%",
                style = HaloType.body03Regular,
                color = Primary30,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

fun progressString(progress: Int): String {
    val koreanOrders = listOf(
        "첫", "두", "세", "네", "다섯",
        "여섯", "일곱", "여덟", "아홉"
    )

    return when (progress) {
        0 -> "아직 첫 장을 펼치지 않았어요"
        10 -> "모든 페이지를 완성했어요!"
        in 1..9 -> "${koreanOrders[progress-1]} 번째 장을 완성했어요"
        else -> ""
    }
}

