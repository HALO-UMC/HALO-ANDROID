package com.umc.halo.presentation.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.HaloType

// 카카오 공식 브랜드 색상
internal val KakaoYellow = Color(0xFFFEE500)
internal val KakaoLabel = Color(0xFF191919)   // 카카오 로고 라벨 색

private val ButtonHeight = 54.dp
private val ButtonCornerRadius = 30.dp
private val IconSlotSize = 24.dp

/**
 * 카카오/구글 로그인 버튼 (로그인·재로그인 화면 공통)

 * @param iconSize 슬롯 안에 그릴 실제 로고 크기
 * @param enabled  false 면 클릭이 막히고 브랜드 색이 살짝 흐려짐
 */
@Composable
internal fun SocialLoginButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    iconSize: Dp = IconSlotSize,
    border: BorderStroke? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight),
        shape = RoundedCornerShape(ButtonCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            // 비활성 상태에서 살짝 흐리게만
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        border = border
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(IconSlotSize),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,      // 장식용 로고라 null
                    modifier = Modifier.size(iconSize)
                )
            }
            Text(
                text = text,
                style = HaloType.body01Medium.copy(letterSpacing = Tracking1Percent),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
