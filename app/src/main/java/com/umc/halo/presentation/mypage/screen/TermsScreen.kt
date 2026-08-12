package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.SectionTitle
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType

@Composable
fun TermsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val termsLinks = listOf(
        "https://halo-homepage.vercel.app/terms",
        "https://halo-homepage.vercel.app/privacy",
        "https://halo-homepage.vercel.app/content-policy",
        "https://halo-homepage.vercel.app/marketing"
    )

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "이용 약관", onBack = onBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp, bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionTitle("세부 약관")
                Spacer(Modifier.height(18.dp))
                listOf(
                    "서비스 이용약관",
                    "개인정보 처리방침",
                    "콘텐츠 보관 및 활용 안내",
                    "마케팅 정보 수신 동의"
                ).forEachIndexed { index, title ->
                    TermsAgreementRow(
                        title = title,
                        onClick = { uriHandler.openUri(termsLinks[index]) }
                    )
                    if (index != 3) {
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            Text(
                text = "최종 업데이트 | 26. 06.26",
                style = HaloType.body03Regular,
                color = Gray300,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun TermsAgreementRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = HaloType.body02SemiBold,
            color = Gray400,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_chevron_right),
                contentDescription = null,
                tint = Gray400,
                modifier = Modifier.size(8.dp, 12.dp)
            )
        }
    }
}
