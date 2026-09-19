package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.component.MyPageBrandTopBar
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.ProfileCard
import com.umc.halo.presentation.mypage.component.SectionTitle
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun MyPageScreen(
    uiState: MyPageUiState,
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
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp, bottom = 32.dp)
        ) {
            SectionTitle("내 정보")
            Spacer(Modifier.height(12.dp))
            ProfileCard(
                name = uiState.memberName,
                birthDate = uiState.memberBirthDate,
                characterImageUrl = uiState.memberCharacterImageUrl
            )

            Spacer(Modifier.height(36.dp))
            SectionTitle("추가 기능")
            Spacer(Modifier.height(18.dp))
            MenuSection(dividerGap = 8.dp) {
                HomeMenuRow(title = "관계 정보", onClick = onNavigateToRelationshipInfo)
                MenuDivider()
                HomeMenuRow(title = "기념일 관리", onClick = onNavigateToAnniversary)
            }

            Spacer(Modifier.height(52.dp))
            SectionTitle("기본 설정")
            Spacer(Modifier.height(18.dp))
            MenuSection(dividerGap = 5.dp) {
                HomeMenuRow(title = "시스템 설정", onClick = onNavigateToSystemSettings)
                MenuDivider()
                HomeMenuRow(title = "알림 설정", onClick = onNavigateToNotificationSettings)
                MenuDivider()
                HomeMenuRow(title = "계정 관리", onClick = onNavigateToAccountManagement)
            }
        }
    }
}

@Composable
private fun MenuSection(
    dividerGap: Dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dividerGap)
    ) {
        content()
    }
}

@Composable
private fun HomeMenuRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = HaloType.body02Medium,
            color = Gray800,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_common_chevron_right),
            contentDescription = null,
            tint = Gray700,
            modifier = Modifier.size(8.dp, 12.dp)
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(color = Gray100)
}
