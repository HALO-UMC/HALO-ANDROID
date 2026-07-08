package com.umc.halo.presentation.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType

// 카카오 공식 브랜드 색상
private val KakaoYellow = Color(0xFFFEE500)
private val KakaoLabel = Color(0xD9000000)      // 검정 85%
private val GoogleBorder = Color(0xFFDADADA)

/**
 * 온보딩 마지막 단계 - 소셜 로그인 화면
 * onKakaoClick / onGoogleClick 버튼을 터치할 때 시그널을 외부로 보냄
 */
@Composable
fun LoginScreen(
    onKakaoClick: () -> Unit,
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 21.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.weight(2.3f))

        Text(
            text = "모두 준비되었어요!",
            style = HaloType.heading01SemiBold,
            color = Gray800
        )

        Spacer(Modifier.weight(1.5f))

        Text(
            text = "당신의 이야기를\nHALO와 함께 시작해볼까요?",
            style = HaloType.body01Medium,
            color = Gray800,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        // 카카오 로그인 버튼
        SocialLoginButton(
            text = "카카오 로그인",
            containerColor = KakaoYellow,
            contentColor = KakaoLabel,
            iconRes = R.drawable.ic_loginscreen_kakao_logo,
            onClick = onKakaoClick
        )

        Spacer(Modifier.height(12.dp))

        // 구글 로그인 버튼
        SocialLoginButton(
            text = "Google로 로그인",
            containerColor = Color.White,
            contentColor = Color.Black,
            iconRes = R.drawable.ic_google,
            border = BorderStroke(1.dp, GoogleBorder),
            onClick = onGoogleClick
        )

        Spacer(Modifier.height(17.dp))

        Text(
            text = "계속 진행하면 이용약관과 개인정보처리방침에 동의하게 됩니다.",
            style = HaloType.caption01Medium,
            color = Gray400,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(76.dp))
    }
}

/**
 * 카카오/구글 버튼(공통)
 */
@Composable
private fun SocialLoginButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,          // 장식용 로고라 null
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = HaloType.body01Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    HaloTheme {
        LoginScreen(
            onKakaoClick = {},
            onGoogleClick = {}
        )
    }
}