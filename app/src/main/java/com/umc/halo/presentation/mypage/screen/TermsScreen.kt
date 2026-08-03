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
import com.umc.halo.presentation.mypage.component.MenuRow
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.SectionTitle
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType

@Composable
fun TermsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "이용 약관", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp)
        ) {
            Text(
                text = "최종 업데이트 | 26. 06.26",
                style = HaloType.body03Regular,
                color = Gray500
            )
            Spacer(Modifier.height(28.dp))
            SectionTitle("세부 약관")
            Spacer(Modifier.height(18.dp))
            listOf(
                "서비스 이용약관",
                "개인정보 처리방침",
                "콘텐츠 보관 및 활용 안내",
                "마케팅 정보 수신 동의"
            ).forEach { title ->
                MenuRow(
                    title = title,
                    titleColor = Gray400,
                    onClick = {}
                )
            }
        }
    }
}
