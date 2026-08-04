package com.umc.halo.presentation.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.calendar.CompletedBook
import com.umc.halo.domain.model.calendar.MonthSummary
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 책등 이미지(홈 책장과 같은 자산 사용 — 투명 배경)
// 순서는 홈 HomeViewModel 의 번호와 동일: 1 오래전 당신 / 2 당신의 1호 팬 / 3 취향이 닿는 날 ...
private val StorybookSpines = listOf(
    R.drawable.image_home_bookcase_1, R.drawable.image_home_bookcase_2, R.drawable.image_home_bookcase_3,
    R.drawable.image_home_bookcase_4, R.drawable.image_home_bookcase_5, R.drawable.image_home_bookcase_6,
    R.drawable.image_home_bookcase_7, R.drawable.image_home_bookcase_8, R.drawable.image_home_bookcase_9,
    R.drawable.image_home_bookcase_10
)
private val ShelfHeight = 84.dp

/**
 * 하단 "N월 한달 요약" 카드.
 * - 완료한 책이 있으면(summary.isEmpty == false): 완성 + 진행중 + 책꽂이 + 자세히 보러가기
 * - 완료한 책이 없으면: 완성(0개) + '바로 시작하기' CTA
 */
@Composable
fun CalendarMonthSummary(
    month: Int,
    summary: MonthSummary,
    onEvent: (CalendarUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Gray30)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${month}월 한달 요약",
            style = HaloType.body01SemiBold,
            color = Gray800
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCountRow(
                label = "완성 스토리북",
                count = summary.completedCount,
                iconRes = R.drawable.ic_calendar_storybook_completed_mark,
                countColor = Primary500,
                countStyle = if (summary.isEmpty) HaloType.heading02SemiBold else HaloType.heading03SemiBold
            )

            if (summary.isEmpty) {
                StartRecordingCta(onClick = { onEvent(CalendarUiEvent.OnStartRecordingClicked) })
            } else {
                SummaryCountRow(
                    label = "진행 중 스토리북",
                    count = summary.inProgressCount,
                    iconRes = R.drawable.ic_calendar_storybook_inprogress_mark,
                    countColor = Gray600,
                    countStyle = HaloType.heading03SemiBold
                )
                BookShelfCard(
                    books = summary.completedBooks,
                    onDetailClick = { onEvent(CalendarUiEvent.OnSummaryDetailClicked) }
                )
            }
        }
    }
}

@Composable
private fun SummaryCountRow(
    label: String,
    count: Int,
    iconRes: Int,
    countColor: Color,
    countStyle: TextStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = HaloType.body02Medium,
            color = Gray600,
            modifier = Modifier.weight(1f)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = "${count}개",
                style = countStyle,
                color = countColor
            )
        }
    }
}

/** '바로 시작하기' 전체폭 CTA (완성/진행중 스토리북이 모두 없을 때) */
@Composable
private fun StartRecordingCta(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Primary50)
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "바로 시작하기",
            style = HaloType.body02Medium,
            color = Gray800,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Gray800,
            modifier = Modifier.size(24.dp)
        )
    }
}

/** 완료한 스토리북 책꽂이 + '자세히 보러가기 >' */
@Composable
private fun BookShelfCard(
    books: List<CompletedBook>,
    onDetailClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 완료 스토리북 책등(완료된 책만)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ShelfHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            books.forEachIndexed { index, _ ->
                BookSpine(spineRes = StorybookSpines[index % StorybookSpines.size])
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDetailClick() },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "자세히 보러가기 >",
                style = HaloType.body02Medium,
                color = Gray500
            )
        }
    }
}

@Composable
private fun BookSpine(spineRes: Int) {
    val painter = painterResource(spineRes)
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(ShelfHeight)
            .aspectRatio(
                ratio = painter.intrinsicSize.width / painter.intrinsicSize.height,
                matchHeightConstraintsFirst = true
            )
    )
}
