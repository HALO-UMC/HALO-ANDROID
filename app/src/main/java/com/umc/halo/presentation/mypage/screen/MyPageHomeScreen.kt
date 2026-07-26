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
import com.umc.halo.presentation.mypage.component.MenuRow
import com.umc.halo.presentation.mypage.component.MyPageBrandTopBar
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.ProfileCard
import com.umc.halo.presentation.mypage.component.SectionTitle

@Composable
fun MyPageScreen(
    onNavigateToRelationshipInfo: () -> Unit,
    onNavigateToAnniversary: () -> Unit,
    onNavigateToSystemSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit,
    onNavigateToAccountManagement: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageBrandTopBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            SectionTitle("내 정보")
            Spacer(Modifier.height(14.dp))
            ProfileCard()

            Spacer(Modifier.height(32.dp))
            SectionTitle("추가 기능")
            Spacer(Modifier.height(14.dp))
            MenuRow(title = "관계 정보", onClick = onNavigateToRelationshipInfo)
            MenuRow(title = "기념일 관리", onClick = onNavigateToAnniversary)

            Spacer(Modifier.height(24.dp))
            SectionTitle("기본 설정")
            Spacer(Modifier.height(14.dp))
            MenuRow(title = "시스템 설정", onClick = onNavigateToSystemSettings)
            MenuRow(title = "알림 설정", onClick = onNavigateToNotificationSettings)
            MenuRow(title = "계정 관리", onClick = onNavigateToAccountManagement)
        }
    }
}
