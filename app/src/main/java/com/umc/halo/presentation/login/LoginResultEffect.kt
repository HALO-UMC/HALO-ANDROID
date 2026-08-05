package com.umc.halo.presentation.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.umc.halo.domain.model.auth.AuthDestination

/**
 * 로그인 결과 뒷처리 (로그인 화면 / 재로그인 화면 공통)
 *
 * 두 화면은 로그인 로직은 같아 [LoginViewModel] 을 함께 사용
 * 성공 / 실패 시 결과가 같으므로 한 곳에 모아두고 두 Route 가 호출
 *
 * 이동/표시를 끝낸 뒤에는 상태를 되돌리는 이벤트를 보내 같은 신호가 두 번 처리되지 않게 함
 */
@Composable
internal fun LoginResultEffect(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.destination) {
        when (uiState.destination ?: return@LaunchedEffect) {
            AuthDestination.TERMS -> onNavigateToTerms()
            AuthDestination.ONBOARDING -> onNavigateToOnboarding()
            AuthDestination.HOME -> onNavigateToHome()
            // 로그인에 성공한 뒤 다시 로그인으로 보낼 일은 없으므로
            AuthDestination.LOGIN -> Unit
        }
        onEvent(LoginUiEvent.NavigationHandled)
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        // TODO: 에러 표시 방식은 디자인 확정 후 교체
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        onEvent(LoginUiEvent.ErrorShown)
    }
}
