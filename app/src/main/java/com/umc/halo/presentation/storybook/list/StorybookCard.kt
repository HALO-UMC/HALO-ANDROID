package com.umc.halo.presentation.storybook.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

/**
* 스토리북 카드 디자인 값
*/

private val CardAspectRatio = 131f / 166f            // 가로:세로 (세로형 비율 잠금)
private val CardCornerRadius = 8.dp
private val CardElevation = 4.dp
private const val CoverWeight = 106f                 // 커버 : 라벨 높이 비율 = 106 : 60
private const val LabelWeight = 60f
private val LabelPaddingHorizontal = 10.dp
private val LabelPaddingVertical = 11.dp
private val CoverPlaceholderColor = Gray100          // TODO: 실제 커버 이미지로 추후 교체
private val WaitingOverlayColor = Color(0x80000000)  // 대기 상태 50% 딤
private val BadgeFillColor = Gray700                 // "N장 진행중" 책갈피 배지 색

/**
 * 스토리북 카드 (전체 / 진행중 / 완료 탭 공통)
 *
 * 크기는 호출부가 [modifier]로 지정
 * 세로 비율은 내부에서 [CardAspectRatio]로 잠그므로 폭만 정해주면 됨
 *
 * @param progressChapter null이 아니면 "N장 진행중" 책갈피 배지를 표시 (진행중 탭)
 * @param isWaiting true면 오늘 분량 완료 상태 → 카드를 어둡게 하고 안내 문구 표시 + 클릭 비활성화
 * @param onClick null이거나 isWaiting=true면 클릭되지 않음
 */
@Composable
fun StorybookCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    progressChapter: Int? = null,
    isWaiting: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(CardCornerRadius)
    Box(
        modifier = modifier
            .aspectRatio(CardAspectRatio)
            .shadow(elevation = CardElevation, shape = shape)
            .clip(shape)
            .background(White)
            // 대기 상태이거나 onClick이 없으면 클릭 비활성화
            .then(
                if (onClick != null && !isWaiting) Modifier.clickable { onClick() }
                else Modifier
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 커버 이미지 자리(임시) + 진행 배지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(CoverWeight)
                    .background(CoverPlaceholderColor)
            ) {
                if (progressChapter != null && !isWaiting) {
                    ProgressBadge(
                        text = "${progressChapter}장 진행중",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 4.dp)
                    )
                }
            }

            // 하단 흰색 라벨(제목/부제)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(LabelWeight)
                    .background(White)
                    .padding(
                        horizontal = LabelPaddingHorizontal,
                        vertical = LabelPaddingVertical
                    )
            ) {
                Text(
                    text = title,
                    style = HaloType.body01SemiBold,
                    color = Gray800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = HaloType.caption01Regular,
                    color = Gray700
                )
            }
        }

        // 오늘 분량 완료 상태: 카드 전체를 어둡게 하고 안내 문구 표시
        if (isWaiting) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape)
                    .background(WaitingOverlayColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "다음 장은\n내일 할 수 있어요!",
                    style = HaloType.body03Medium,
                    color = White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 커버 위 진행 상태인 책갈피 배지
 */
@Composable
private fun ProgressBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(BadgeFillColor, ProgressBadgeShape)
            .padding(start = 8.dp, end = 18.dp, top = 3.dp, bottom = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = HaloType.caption01Medium.copy(fontSize = 9.sp),
            color = White
        )
    }
}

// 진행 배지 책갈피 모양
private val ProgressBadgeShape = GenericShape { size, _ ->
    val sx = size.width / 61f
    val sy = size.height / 20f
    moveTo(0f, 4f * sy)
    cubicTo(0f, 1.79f * sy, 1.79f * sx, 0f, 4f * sx, 0f)
    lineTo(43.91f * sx, 0f)
    cubicTo(45.17f * sx, 0f, 46.35f * sx, 0.59f * sy, 47.11f * sx, 1.59f * sy)
    lineTo(61f * sx, 20f * sy)
    lineTo(0f, 20f * sy)
    close()
}
