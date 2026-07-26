package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.component.ConfirmActionDialog
import com.umc.halo.presentation.mypage.component.InfoRow
import com.umc.halo.presentation.mypage.component.MenuRow
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.SectionTitle

@Composable
fun AccountInfoScreen(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showLogoutDialog) {
        ConfirmActionDialog(
            title = "정말 로그아웃하시겠습니까?",
            description = "현재 계정에서 로그아웃 되며 재로그인이 필요해요.",
            buttonText = "로그아웃 하기",
            onDismiss = {
                onEvent(MyPageUiEvent.LogoutDialogChanged(false))
            },
            onConfirm = onNavigateToLogin
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "계정 정보", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 30.dp, bottom = 32.dp)
        ) {
            SectionTitle("계정 정보")
            Spacer(Modifier.height(20.dp))
            InfoRow(label = "닉네임", value = "난혁")
            InfoRow(label = "로그인 방식", value = "카카오 로그인")
            InfoRow(label = "이메일", value = "kimjooyeon038@gmail.com")
            InfoRow(label = "계정 생성일", value = "2026.06.28")

            Spacer(Modifier.height(40.dp))
            SectionTitle("서비스 이용 정보")
            Spacer(Modifier.height(20.dp))
            InfoRow(label = "알림 상태", value = "수신 중")
            MenuRow(
                title = "로그 아웃",
                onClick = {
                    onEvent(MyPageUiEvent.LogoutDialogChanged(true))
                }
            )
            MenuRow(title = "회원 탈퇴", onClick = onNavigateToWithdraw)
        }
    }
}
