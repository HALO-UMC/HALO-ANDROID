package com.umc.halo.presentation.mypage

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.presentation.mypage.anniversary.AnniversaryScreenMode
import com.umc.halo.presentation.mypage.anniversary.AnniversaryUiEvent
import com.umc.halo.presentation.mypage.anniversary.AnniversaryViewModel
import com.umc.halo.presentation.mypage.relationship.RelationshipInfoViewModel
import com.umc.halo.presentation.mypage.screen.AccountInfoScreen
import com.umc.halo.presentation.mypage.screen.AnniversaryScreen
import com.umc.halo.presentation.mypage.screen.NotificationSettingsScreen
import com.umc.halo.presentation.mypage.screen.RelationshipInfoScreen
import com.umc.halo.presentation.mypage.screen.SystemSettingsScreen
import com.umc.halo.presentation.mypage.screen.WithdrawScreen

@Composable
fun SystemSettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.systemSettingsErrorMessage) {
        val message = uiState.systemSettingsErrorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(MyPageUiEvent.SystemSettingsErrorShown)
    }

    SystemSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun NotificationSettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NotificationSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun AccountInfoRoute(
    onBack: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 로그아웃 처리(서버 + 토큰 삭제)가 끝난 뒤에 이동
    AccountActionEffect(uiState = uiState, viewModel = viewModel, onNavigateToLogin = onNavigateToLogin)

    AccountInfoScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToWithdraw = onNavigateToWithdraw,
        modifier = modifier
    )
}

@Composable
fun WithdrawRoute(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 탈퇴 처리(서버 계정 삭제 + 소셜 연결 해제)가 끝난 뒤에 이동
    AccountActionEffect(uiState = uiState, viewModel = viewModel, onNavigateToLogin = onNavigateToLogin)

    WithdrawScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}

/**
 * 로그아웃/탈퇴 결과를 화면 이동과 안내로 연결하는 공통 처리
 * (계정 정보 화면과 탈퇴 화면이 같은 방식이라 하나로 묶음)
 */
@Composable
private fun AccountActionEffect(
    uiState: MyPageUiState,
    viewModel: MyPageViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.navigateToLogin) {
        if (!uiState.navigateToLogin) return@LaunchedEffect
        viewModel.onEvent(MyPageUiEvent.AccountNavigationHandled)
        onNavigateToLogin()
    }

    LaunchedEffect(uiState.accountErrorMessage) {
        val message = uiState.accountErrorMessage ?: return@LaunchedEffect
        // TODO: 에러 표시 방식은 디자인 확정 후 교체
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(MyPageUiEvent.AccountErrorShown)
    }
}

@Composable
fun RelationshipInfoRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RelationshipInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.errorMessageShown()
    }

    RelationshipInfoScreen(
        uiState = uiState,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun AnniversaryRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnniversaryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        if (uiState.mode == AnniversaryScreenMode.LIST) {
            viewModel.onEvent(AnniversaryUiEvent.ListExited)
            onBack()
        } else {
            viewModel.onEvent(AnniversaryUiEvent.BackClicked)
        }
    }

    AnniversaryScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = {
            if (uiState.mode == AnniversaryScreenMode.LIST) {
                viewModel.onEvent(AnniversaryUiEvent.ListExited)
                onBack()
            } else {
                viewModel.onEvent(AnniversaryUiEvent.BackClicked)
            }
        },
        modifier = modifier
    )
}
