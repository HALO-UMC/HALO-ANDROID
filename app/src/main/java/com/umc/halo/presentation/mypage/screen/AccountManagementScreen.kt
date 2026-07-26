package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.mypage.component.MenuRow
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar

@Composable
fun AccountManagementScreen(
    onBack: () -> Unit,
    onNavigateToAccountInfo: () -> Unit,
    onNavigateToOpenLicense: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "계정 관리", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 38.dp)
        ) {
            MenuRow(title = "계정 정보", onClick = onNavigateToAccountInfo)
            MenuRow(title = "오픈 라이선스", onClick = onNavigateToOpenLicense)
            MenuRow(title = "개인정보 처리방침", onClick = onNavigateToPrivacyPolicy)
            MenuRow(title = "이용 약관", onClick = onNavigateToTerms)
        }
    }
}
