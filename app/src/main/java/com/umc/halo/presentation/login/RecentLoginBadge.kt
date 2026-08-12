package com.umc.halo.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary600

private val BadgeCornerRadius = 4.dp
private val BadgeHorizontalPadding = 12.dp
private val BadgeVerticalPadding = 6.dp
private val BadgeTailWidth = 18.5.dp
private val BadgeTailHeight = 9.3.dp

/**
 * 재로그인 화면에서 직전에 사용한 소셜 버튼 위에 띄우는 '최근 로그인' 베지
 */
@Composable
internal fun RecentLoginBadge(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 말풍선 몸통
        Box(
            modifier = Modifier
                .background(Primary100, RoundedCornerShape(BadgeCornerRadius))
                .padding(
                    horizontal = BadgeHorizontalPadding,
                    vertical = BadgeVerticalPadding
                )
        ) {
            Text(
                text = "최근 로그인",
                style = HaloType.body03Regular.copy(letterSpacing = Tracking1Percent),
                color = Primary600
            )
        }

        Box(
            modifier = Modifier
                .width(BadgeTailWidth)
                .height(BadgeTailHeight)
                .background(Primary100, BadgeTailShape)
        )
    }
}

/** 말풍선 꼬리 */
private val BadgeTailShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

@Preview(showBackground = true)
@Composable
private fun RecentLoginBadgePreview() {
    HaloTheme {
        RecentLoginBadge()
    }
}
