package com.umc.halo.presentation.storybook.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

/**
* 스토리북 카드 디자인 값
*/

private val CardAspectRatio = 147f / 185f            // 가로:세로 (세로형 비율 잠금)
private val CardCornerRadius = 8.dp
private val CardElevation = 4.dp
private const val CoverWeight = 119f                 // 커버 : 라벨 높이 비율 = 119 : 66
private const val LabelWeight = 66f
private val LabelPaddingHorizontal = 12.dp
private val LabelPaddingVertical = 13.dp
private val CoverPlaceholderColor = Gray100          // TODO: 실제 커버 이미지로 추후 교체
private val CoverDimColor = Color(0x1A000000)        // 커버 위 10% 딤 (책갈피·커버가 묻히지 않게)
private val WaitingOverlayColor = Color(0x99000000)  // 대기 상태 60% 딤
private val WaitingTextSize = 13.5.sp                // 대기 안내 문구

// 책갈피(배지) 값은 147dp 카드 기준 — 카드 폭을 바꾸면 함께 조정해야 비율이 맞음
private val BadgeStartPadding = 4.5.dp               // 커버 왼쪽 끝 ~ 책갈피 시작
private val BadgeTextSize = 11.2.sp                  // 배지 글자
private val BadgeHeight = 24.7.dp                    // 진행중/완료 공통 높이
private val InProgressBadgeColor = Gray700           // "N장 진행중" 책갈피 색
private val DoneBadgeColor = Primary500              // "완료" 책갈피 색
private val DoneBadgeWidth = 45.dp                   // "완료"는 글자가 고정이라 폭도 고정

/**
 * 커버 위 책갈피 종류
 * 진행중 탭에서는 "N장 진행중" / 완료 텝에서는 "완료"
 */
sealed interface StorybookBadge {
    // 진행중 탭: 현재 진행중인 장수
    data class InProgress(val chapter: Int) : StorybookBadge

    // 완료 탭: 10장까지 모두 끝낸 스토리북
    data object Done : StorybookBadge
}

/**
 * 스토리북 카드 (전체 / 진행중 / 완료 탭 공통)
 *
 * 크기는 호출부가 [modifier]로 지정
 * 세로 비율은 내부에서 [CardAspectRatio]로 잠그므로 폭만 정해주면 됨
 *
 * @param coverUrl 서버가 준 커버 이미지 URL. null·빈 문자열이면 회색 플레이스홀더
 * @param badge null이면 배지 없음(전체 탭). 진행중/완료 탭은 각각 [StorybookBadge]를 넘김
 * @param isWaiting true면 오늘 분량 완료 상태 → 카드를 어둡게 하고 안내 문구 표시 + 클릭 비활성화
 * @param onClick null이거나 isWaiting=true면 클릭되지 않음
 */
@Composable
fun StorybookCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    coverUrl: String? = null,
    badge: StorybookBadge? = null,
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
            // 커버 이미지 + 책갈피 배지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(CoverWeight)
                    .background(CoverPlaceholderColor)
            ) {
                if (!coverUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = coverUrl,
                        contentDescription = null,      // 장식용 이미지라 null
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }

                // 커버 위 딤(디자인 10%) — 커버 유무와 상관없이 항상
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(CoverDimColor)
                )

                if (badge != null && !isWaiting) {
                    BookmarkBadge(
                        badge = badge,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = BadgeStartPadding)
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
                    style = HaloType.body03Regular,
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
                    style = HaloType.body03Medium.copy(
                        fontSize = WaitingTextSize,
                        lineHeight = 19.5.sp
                    ),
                    color = White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 커버 위 책갈피
 */
@Composable
private fun BookmarkBadge(
    badge: StorybookBadge,
    modifier: Modifier = Modifier
) {
    when (badge) {
        // 장수에 따라 글자 길이가 변해서 크기를 가변적으로
        is StorybookBadge.InProgress -> Box(
            modifier = modifier
                .height(BadgeHeight)
                .background(InProgressBadgeColor, InProgressBadgeShape)
                .padding(start = 6.7.dp, end = 22.4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "${badge.chapter}장 진행중",
                style = HaloType.caption01Medium.copy(fontSize = BadgeTextSize),
                color = White
            )
        }

        StorybookBadge.Done -> Box(
            modifier = modifier
                .size(width = DoneBadgeWidth, height = BadgeHeight)
                .background(DoneBadgeColor, DoneBadgeShape)
                .padding(start = 9.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "완료",
                style = HaloType.caption01Medium.copy(fontSize = BadgeTextSize),
                color = White
            )
        }
    }
}


// 진행중 책갈피
private val InProgressBadgeShape = GenericShape { size, _ ->
    val sx = size.width / 66f
    val sy = size.height / 22f
    moveTo(0f, 4f * sy)
    cubicTo(0f, 1.79086f * sy, 1.79086f * sx, 0f, 4f * sx, 0f)
    lineTo(47.6577f * sx, 0f)
    cubicTo(48.9237f * sx, 0f, 50.115f * sx, 0.599345f * sy, 50.8696f * sx, 1.61592f * sy)
    lineTo(66f * sx, 22f * sy)
    lineTo(0f, 22f * sy)
    close()
}

// 완료 책갈피
private val DoneBadgeShape = GenericShape { size, _ ->
    val sx = size.width / 40f
    val sy = size.height / 22f
    moveTo(0f, 4f * sy)
    cubicTo(0f, 1.79086f * sy, 1.79086f * sx, 0f, 4f * sx, 0f)
    lineTo(27.5164f * sx, 0f)
    cubicTo(29.0906f * sx, 0f, 30.5185f * sx, 0.923335f * sy, 31.1643f * sx, 2.35897f * sy)
    lineTo(40f * sx, 22f * sy)
    lineTo(0f, 22f * sy)
    close()
}
