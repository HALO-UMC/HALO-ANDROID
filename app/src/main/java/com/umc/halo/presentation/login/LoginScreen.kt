package com.umc.halo.presentation.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType

// 카카오 공식 브랜드 색상
private val KakaoYellow = Color(0xFFFEE500)
private val KakaoLabel = Color(0xFF191919)       // 카카오 로고 라벨 색

/**
 * 로그인 화면 진입점
 * NavGraph 에서 이 LoginRoute 호출
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToTerms: () -> Unit = {}
) {
    // 카카오/구글 SDK 호출에 필요한 Activity Context
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // 로그인 성공(현재는 임시 트리거) → 약관동의로 이동
    // TODO: 서버 연동 시 수정해야함
    LaunchedEffect(uiState.navigateToTerms) {
        if (uiState.navigateToTerms) {
            onNavigateToTerms()
            viewModel.onEvent(LoginUiEvent.NavigationHandled)
        }
    }

    LoginScreen(
        onKakaoClick = { viewModel.onEvent(LoginUiEvent.KakaoLoginClicked(context)) },
        onGoogleClick = { viewModel.onEvent(LoginUiEvent.GoogleLoginClicked(context)) },
        modifier = modifier
    )
}

/**
 * 소셜 로그인 화면
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
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 위쪽 여백
        Spacer(Modifier.weight(3f))

        Text(
            text = "가장 가까운 사람을 다시 알아가는 시간",
            style = HaloType.body02Medium.copy(
                lineHeight = 21.sp,
                letterSpacing = (-0.28).sp
            ),
            color = Gray800,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "HALO 시작해볼까요?",
            style = HaloType.heading02Medium.copy(
                fontSize = 24.sp,
                lineHeight = 36.sp,
                letterSpacing = (-0.48).sp
            ),
            color = Gray800,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))

        // 중앙 HALO 캐릭터
        Image(
            painter = painterResource(id = R.drawable.ic_login_character),
            contentDescription = null,          // 장식용 이미지라 null
            modifier = Modifier
                .width(133.dp)
                .height(155.dp)
        )

        // 캐릭터 ~ 버튼 여백
        Spacer(Modifier.weight(1f))

        // 카카오 로그인 버튼
        SocialLoginButton(
            text = "카카오 로그인",
            containerColor = KakaoYellow,
            contentColor = KakaoLabel,
            iconRes = R.drawable.ic_login_kakao_logo,
            iconSize = 20.dp,
            onClick = onKakaoClick
        )

        Spacer(Modifier.height(12.dp))

        // 구글 로그인 버튼
        SocialLoginButton(
            text = "Google로 로그인",
            containerColor = Color.White,
            contentColor = Gray800,
            iconRes = R.drawable.ic_login_google_logo,
            iconSize = 20.dp,       // 로고 파일이 여백 없는 G 글리프라 카카오와 같은 20dp
            border = BorderStroke(1.dp, Gray200),
            onClick = onGoogleClick
        )

        Spacer(Modifier.height(36.dp))

        Text(
            text = "계속 진행하면 이용약관과 개인정보처리방침에 동의하게 됩니다.",
            style = HaloType.caption01Medium.copy(
                lineHeight = 14.5.sp,
                letterSpacing = (-0.1).sp
            ),
            color = Gray400,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(73.dp))
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
    iconSize: Dp = 24.dp,
    border: BorderStroke? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
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
                style = HaloType.body01Medium.copy(
                    lineHeight = 23.2.sp,
                    letterSpacing = (-0.16).sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
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