package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

private data class OpenLicenseItem(
    val name: String,
    val version: String,
    val description: String
)

private val openLicenseItems = listOf(
    OpenLicenseItem(
        name = "AndroidX Core KTX",
        version = "1.12",
        description = "The android,,~"
    ),
    OpenLicenseItem(
        name = "AndroidX Core KTX",
        version = "1.12",
        description = "The android,,~"
    ),
    OpenLicenseItem(
        name = "AndroidX Core KTX",
        version = "1.12",
        description = "The android,,~"
    )
)

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 52.dp, bottom = 32.dp)
        ) {
            Text(
                text = "HALO 는 오픈소스 라이브러리를 사용합니다.\n각 라이브러리의 라이선스 전문은 아래에서 확인하실 수 있습니다.",
                style = HaloType.body03Regular,
                color = Gray500
            )

            Spacer(Modifier.height(38.dp))

            openLicenseItems.forEachIndexed { index, item ->
                OpenLicenseRow(
                    item = item,
                    onClick = {}
                )

                if (index < openLicenseItems.lastIndex) {
                    HorizontalDivider(
                        color = Gray100,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenLicenseRow(
    item: OpenLicenseItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(39.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gray200)
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = item.name,
                    style = HaloType.body02SemiBold,
                    color = Gray800
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = item.version,
                    style = HaloType.body03Regular,
                    color = Gray500
                )
            }

            Spacer(Modifier.height(2.dp))

            Text(
                text = item.description,
                style = HaloType.body03Regular,
                color = Gray500
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Gray800,
            modifier = Modifier.size(24.dp)
        )
    }
}
