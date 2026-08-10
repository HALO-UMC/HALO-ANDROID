package com.umc.halo.presentation.storybook.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.umc.halo.presentation.component.haloCardShadow
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
private const val CoverWeight = 119f                 // 커버 : 라벨 높이 비율 = 119 : 66
private const val LabelWeight = 66f
private val LabelPaddingHorizontal = 12.dp
private val LabelPaddingVertical = 13.dp
private val CoverPlaceholderColor = Gray100          // 커버 로딩 중,로드 실패 시 뒤에 깔리는 배경
private val CoverDimColor = Color(0x1A000000)        // 커버 위 10% 딤 (책갈피·커버가 묻히지 않게)
private val WaitingOverlayColor = Color(0x99000000)  // 대기 상태 60% 딤
private val WaitingTextSize = 13.5.sp                // 대기 안내 문구

// 책갈피(배지) — 진행중/완료 공통
private val BadgeStartPadding = 8.dp                 // 커버 왼쪽 끝 ~ 책갈피 시작
private val BadgeTailWidth = 12.dp                   // 오른쪽으로 뾰족하게 빠지는 꼬리 폭
private val BadgeCornerRadius = 4.dp                 // 왼쪽 위 모서리만 둥굴게
// 패딩: 위 4 / 왼쪽 6 / 아래 4
private val BadgeTopPadding = 4.dp
private val BadgeStartInnerPadding = 6.dp
private val BadgeBottomPadding = 4.dp
private val InProgressBadgeColor = Gray700           // "N장 진행중" 책갈피 색
private val DoneBadgeColor = Primary500              // "완료" 책갈피 색

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
 * @param isWaiting true면 오늘 분량 완료(서버 status=TODAY_DONE) 상태
 * @param onClick null이면 클릭되지 않음
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
            .haloCardShadow(shape)
            .clip(shape)
            .background(White)
            // onClick이 없을 때만 클릭 비활성화
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
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
 *
 * 구성: [왼쪽 위만 둥근 사각형 몸통] + [오른쪽으로 뾰족하게 빠지는 꼬리]
 * 높이는 글자 크기 + 위아래 패딩으로 정해지고, 꼬리가 그 높이에 맞춰 늘어난다
 */
@Composable
private fun BookmarkBadge(
    badge: StorybookBadge,
    modifier: Modifier = Modifier
) {
    val label = when (badge) {
        is StorybookBadge.InProgress -> "${badge.chapter}장 진행중"
        StorybookBadge.Done -> "완료"
    }
    val badgeColor = when (badge) {
        is StorybookBadge.InProgress -> InProgressBadgeColor
        StorybookBadge.Done -> DoneBadgeColor
    }

    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Box(
            modifier = Modifier
                .background(badgeColor, RoundedCornerShape(topStart = BadgeCornerRadius))
                .padding(
                    start = BadgeStartInnerPadding,
                    top = BadgeTopPadding,
                    bottom = BadgeBottomPadding
                )
        ) {
            Text(
                text = label,
                style = HaloType.body03Medium,
                color = White
            )
        }

        Box(
            modifier = Modifier
                .width(BadgeTailWidth)
                .fillMaxHeight()
                .background(badgeColor, BadgeTailShape)
        )
    }
}

/**
 * 책갈피 오른쪽 꼬리
 */
private val BadgeTailShape = GenericShape { size, _ ->
    val sx = size.width / 12f
    val sy = size.height / 25f
    moveTo(0f, 0f)
    lineTo(0.0664232f * sx, 0f)
    cubicTo(
        1.74608f * sx, 0f,
        3.24682f * sx, 1.04935f * sy,
        3.82338f * sx, 2.62695f * sy
    )
    lineTo(12f * sx, 25f * sy)
    lineTo(0f, 25f * sy)
    close()
}
