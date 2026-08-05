package com.umc.halo.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.R
import com.umc.halo.domain.model.auth.SocialProvider
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType

private const val TopSpaceWeight = 2.25f
private const val CharacterToButtonWeight = 1f

private val TitleToCharacterSpacing = 40.dp
private val CharacterWidth = 132.4.dp
private val CharacterHeight = 153.9.dp
private val BadgeToButtonSpacing = 6.dp
private val ButtonSpacing = 12.dp
private val ButtonToNoticeSpacing = 36.dp
private val BottomSpacing = 53.dp

/**
 * 재로그인 화면 진입점
 * NavGraph 에서 이 ReloginRoute 를 호출
 */
@Composable
fun ReloginRoute(
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

    ReloginScreen(
        isLoading = uiState.isLoading,
        recentProvider = uiState.recentProvider,
        onKakaoClick = { viewModel.onEvent(LoginUiEvent.KakaoLoginClicked(context)) },
        onGoogleClick = { viewModel.onEvent(LoginUiEvent.GoogleLoginClicked(context)) },
        modifier = modifier
    )
}

/**
 * 재로그인 화면 (로그아웃 후 돌아온 사용자)
 *
 * @param isLoading      로그인 진행 중이면 버튼을 잠그고 로딩 표시를 띄움(중복 요청 방지)
 * @param recentProvider 직전에 사용한 로그인 방식. 그 버튼 위에 '최근 로그인' 배지를 띄운다.
 *                       null(로그인 기록 없음)이면 배지를 그리지 않음
 */
@Composable
fun ReloginScreen(
    onKakaoClick: () -> Unit,
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    recentProvider: SocialProvider? = null
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
                text = "다시 돌아온 걸 환영해요!",
                style = HaloType.body02Medium,
                color = Gray800,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "HALO 다시 시작해볼까요?",
                style = HaloType.heading01Regular.copy(fontWeight = FontWeight.Medium),
                color = Gray800,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(TitleToCharacterSpacing))

            // 중앙 HALO 캐릭터
            Image(
                painter = painterResource(id = R.drawable.ic_relogin_character),
                contentDescription = null,          // 장식용 이미지라 null
                modifier = Modifier.size(width = CharacterWidth, height = CharacterHeight)
            )

            // 캐릭터 ~ 버튼 여백
            Spacer(Modifier.weight(CharacterToButtonWeight))

            RecentLoginBadgeSlot(isVisible = recentProvider == SocialProvider.KAKAO)

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

            RecentLoginBadgeSlot(isVisible = recentProvider == SocialProvider.GOOGLE)

            // 구글 로그인 버튼
            SocialLoginButton(
                text = "Google로 로그인",
                containerColor = Color.White,
                contentColor = Gray800,
                iconRes = R.drawable.ic_login_google_logo,
                iconSize = 20.dp,
                border = BorderStroke(1.dp, Gray200),
                enabled = !isLoading,
                onClick = onGoogleClick
            )

            Spacer(Modifier.height(ButtonToNoticeSpacing))

            Text(
                text = "계속 진행하면 이용약관과 개인정보처리방침에 동의하게 됩니다.",
                style = HaloType.caption01Medium,
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

/**
 * 버튼 바로 위의 '최근 로그인' 자리
 */
@Composable
private fun ColumnScope.RecentLoginBadgeSlot(isVisible: Boolean) {
    if (!isVisible) return

    RecentLoginBadge(modifier = Modifier.align(Alignment.End))
    Spacer(Modifier.height(BadgeToButtonSpacing))
}

@Preview(showBackground = true, showSystemUi = true, name = "재로그인 - 최근 카카오")
@Composable
private fun ReloginScreenKakaoPreview() {
    HaloTheme {
        ReloginScreen(
            onKakaoClick = {},
            onGoogleClick = {},
            recentProvider = SocialProvider.KAKAO
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "재로그인 - 최근 구글")
@Composable
private fun ReloginScreenGooglePreview() {
    HaloTheme {
        ReloginScreen(
            onKakaoClick = {},
            onGoogleClick = {},
            recentProvider = SocialProvider.GOOGLE
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "재로그인 - 기록 없음")
@Composable
private fun ReloginScreenNoRecentPreview() {
    HaloTheme {
        ReloginScreen(
            onKakaoClick = {},
            onGoogleClick = {}
        )
    }
}
