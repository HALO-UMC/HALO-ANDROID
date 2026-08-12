package com.umc.halo.presentation.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType

/**
 * 로그인 화면 세로 여백
 */
private const val TopSpaceWeight = 169f          // 상태바 아래 ~ 문구
private const val TextToCharacterWeight = 23.7f  // 문구 ~ 캐릭터
private const val CharacterToButtonWeight = 30f  // 캐릭터 ~ 카카오 버튼
private const val ButtonToTermsWeight = 36.25f   // 구글 버튼 ~ 약관 문구

private val TextLineSpacing = 4.dp               // 문구 두 줄 사이 (고정)
private val ButtonSpacing = 12.dp                // 카카오 ~ 구글 버튼 (고정)
private val BottomSpacing = 53.dp                // 약관 문구 ~ 네비게이션바 (고정, 시스템바 안전 여백)

private val CharacterOffsetX = 12.dp             // 캐릭터를 중앙에서 오른쪽으로 밀어놓은 값

/**
 * 로그인·재로그인 화면 자간
 */
internal val Tracking1Percent: TextUnit = (-0.01).em   // 문구·버튼·약관·배지
internal val Tracking2Percent: TextUnit = (-0.02).em   // 제목 (Heading01/200)

/**
 * 로그인 화면 진입점
 * NavGraph 에서 이 LoginRoute 호출
 *
 * 로그인에 성공하면 약관/온보딩 상태에 따라 세 화면 중 하나로 보냄
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToTerms: () -> Unit = {},
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    // 카카오/구글 SDK 호출에 필요한 Activity Context
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LoginResultEffect(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToTerms = onNavigateToTerms,
        onNavigateToOnboarding = onNavigateToOnboarding,
        onNavigateToHome = onNavigateToHome
    )

    LoginScreen(
        isLoading = uiState.isLoading,
        onKakaoClick = { viewModel.onEvent(LoginUiEvent.KakaoLoginClicked(context)) },
        onGoogleClick = { viewModel.onEvent(LoginUiEvent.GoogleLoginClicked(context)) },
        modifier = modifier
    )
}

/**
 * 소셜 로그인 화면
 * onKakaoClick / onGoogleClick 버튼을 터치할 때 시그널을 외부로 보냄
 *
 * @param isLoading 로그인 진행 중이면 버튼을 잠그고 로딩 표시를 띄움(중복 요청 방지)
 */
@Composable
fun LoginScreen(
    onKakaoClick: () -> Unit,
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 위쪽 여백
        Spacer(Modifier.weight(TopSpaceWeight))

        Text(
            text = "가장 가까운 사람을 다시 알아가는 시간",
            style = HaloType.body02Medium.copy(letterSpacing = Tracking1Percent),
            color = Gray800,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(TextLineSpacing))

        Text(
            text = "HALO 시작해볼까요?",
            style = HaloType.heading01Regular.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = Tracking2Percent
            ),
            color = Gray800,
            textAlign = TextAlign.Center
        )

        // 문구 ~ 캐릭터 여백
        Spacer(Modifier.weight(TextToCharacterWeight))

        // 중앙 HALO 캐릭터
        Image(
            painter = painterResource(id = R.drawable.ic_login_character),
            contentDescription = null,          // 장식용 이미지라 null
            modifier = Modifier
                .offset(x = CharacterOffsetX)
                .size(238.dp)
        )

        // 캐릭터 ~ 버튼 여백
        Spacer(Modifier.weight(CharacterToButtonWeight))

        // 카카오 로그인 버튼
        SocialLoginButton(
            text = "카카오 로그인",
            containerColor = KakaoYellow,
            contentColor = KakaoLabel,
            iconRes = R.drawable.ic_login_kakao_logo,
            iconSize = 20.dp,
            enabled = !isLoading,
            onClick = onKakaoClick
        )

        Spacer(Modifier.height(ButtonSpacing))

        // 구글 로그인 버튼
        SocialLoginButton(
            text = "Google로 로그인",
            containerColor = Color.White,
            contentColor = Gray800,
            iconRes = R.drawable.ic_login_google_logo,
            iconSize = 20.dp,       // 로고 파일이 여백 없는 G 글리프라 카카오와 같은 20dp
            border = BorderStroke(1.dp, Gray200),
            enabled = !isLoading,
            onClick = onGoogleClick
        )

        // 구글 버튼 ~ 약관 문구 여백
        Spacer(Modifier.weight(ButtonToTermsWeight))

        Text(
            text = "계속 진행하면 이용약관과 개인정보처리방침에 동의하게 됩니다.",
            style = HaloType.caption01Medium.copy(letterSpacing = Tracking1Percent),
            color = Gray400,
            textAlign = TextAlign.Center
        )

        // 네비게이션바 위로 띄우는 여백
        Spacer(Modifier.height(BottomSpacing))
    }

        // 로그인 진행 중 표시
        // TODO: 로딩 표현 확정되면 교체
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
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
