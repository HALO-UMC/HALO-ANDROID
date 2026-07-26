package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.mypage.component.MenuRow
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType

@Composable
fun OpenLicenseScreen(
    onBack: () -> Unit,
    onNavigateToAccountInfo: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "오픈라이선스", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            Text(
                text = "항목을 누르면 전문을 볼 수 있어요.",
                style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
                color = Gray700
            )
            Spacer(Modifier.height(28.dp))
            MenuRow(title = "계정 정보", onClick = onNavigateToAccountInfo)
            MenuRow(title = "오픈 라이선스", onClick = {})
            MenuRow(title = "개인정보 처리방침", onClick = onNavigateToPrivacyPolicy)
            MenuRow(title = "이용 약관", onClick = onNavigateToTerms)
        }
    }
}
