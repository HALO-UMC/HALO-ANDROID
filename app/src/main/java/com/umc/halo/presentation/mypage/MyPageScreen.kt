package com.umc.halo.presentation.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.presentation.mypage.anniversary.AnniversaryScreenMode
import com.umc.halo.presentation.mypage.anniversary.AnniversaryUiEvent
import com.umc.halo.presentation.mypage.anniversary.AnniversaryViewModel
import com.umc.halo.presentation.mypage.screen.AccountInfoScreen
import com.umc.halo.presentation.mypage.screen.AnniversaryScreen
import com.umc.halo.presentation.mypage.screen.NotificationSettingsScreen
import com.umc.halo.presentation.mypage.screen.SystemSettingsScreen
import com.umc.halo.presentation.mypage.screen.WithdrawScreen

@Composable
fun SystemSettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
    viewModel: MyPageViewModel = viewModel()
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
    viewModel: MyPageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountInfoScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToWithdraw = onNavigateToWithdraw,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}

@Composable
fun WithdrawRoute(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WithdrawScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToLogin = onNavigateToLogin,
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

    AnniversaryScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = {
            if (uiState.mode == AnniversaryScreenMode.LIST) {
                onBack()
            } else {
                viewModel.onEvent(AnniversaryUiEvent.BackClicked)
            }
        },
        modifier = modifier
    )
}
