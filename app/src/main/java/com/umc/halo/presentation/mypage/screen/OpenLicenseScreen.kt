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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

private data class OpenLicenseItem(
    val name: String,
    val version: String,
    val copyright: String,
    val licenseName: String,
    val licenseResId: Int
)

private val openLicenseItems = listOf(

    OpenLicenseItem(
        name = "AndroidX Core KTX",
        version = "1.18.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Jetpack Compose",
        version = "",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Navigation Compose",
        version = "2.9.7",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Lifecycle",
        version = "2.10.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Activity Compose",
        version = "1.13.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Material 3",
        version = "",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "DataStore",
        version = "1.0.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Credential Manager",
        version = "1.6.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Media3 ExoPlayer",
        version = "1.8.0",
        copyright = "The Android Open Source Project",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Hilt / Dagger",
        version = "2.59.2",
        copyright = "The Dagger Authors",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Retrofit",
        version = "2.9.0",
        copyright = "Copyright 2013 Square, Inc.",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "OkHttp",
        version = "4.12.0",
        copyright = "Square, Inc.",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Gson",
        version = "2.x",
        copyright = "Google Inc.",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Lottie",
        version = "6.6.7",
        copyright = "Airbnb, Inc.",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "DotLottie",
        version = "0.13.8",
        copyright = "LottieFiles",
        licenseName = "MIT License",
        licenseResId = R.raw.mit
    ),

    OpenLicenseItem(
        name = "Coil",
        version = "2.7.0",
        copyright = "Coil Contributors",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Firebase",
        version = "",
        copyright = "Google LLC",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Google Identity",
        version = "1.2.0",
        copyright = "Google LLC",
        licenseName = "Apache License 2.0",
        licenseResId = R.raw.apache_2_0
    ),

    OpenLicenseItem(
        name = "Kakao SDK",
        version = "2.24.0",
        copyright = "Kakao Corp.",
        licenseName = "Kakao Open Source Notice",
        licenseResId = R.raw.kakao_license
    )
)

@Composable
fun OpenLicenseScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLicense by remember {
        mutableStateOf<OpenLicenseItem?>(null)
    }

    MyPageContainer(modifier = modifier) {

        MyPageTopBar(
            title = "오픈라이선스",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 34.dp, bottom = 32.dp)
        ) {
            Text(
                text = "HALO는 오픈소스 라이브러리를 사용합니다.\n각 라이브러리의 라이선스 전문은 아래에서 확인하실 수 있습니다.",
                style = HaloType.body03Regular,
                color = Gray500
            )

            Spacer(Modifier.height(34.dp))

            openLicenseItems.forEachIndexed { index, item ->

                OpenLicenseRow(
                    item = item,
                    onClick = {
                        selectedLicense = item
                    }
                )

                if (index < openLicenseItems.lastIndex) {
                    Spacer(Modifier.height(10.dp))

                    HorizontalDivider(
                        color = Gray100
                    )

                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }

    selectedLicense?.let { license ->

        LicenseDialog(
            license = license,
            onDismiss = {
                selectedLicense = null
            }
        )
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
            .height(84.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = item.name,
                    style = HaloType.body02SemiBold,
                    color = Gray800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = item.version,
                    style = HaloType.body03Regular,
                    color = Gray500,
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(2.dp))

            Text(
                text = "${item.copyright} · ${item.licenseName}",
                style = HaloType.body03Regular,
                color = Gray500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_common_chevron_right
                ),
                contentDescription = null,
                tint = Gray700,
                modifier = Modifier.size(8.dp, 12.dp)
            )
        }
    }
}

@Composable
private fun LicenseDialog(
    license: OpenLicenseItem,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val licenseText = remember(license) {
        context.resources
            .openRawResource(license.licenseResId)
            .bufferedReader()
            .use { it.readText() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Column {
                Text(
                    text = license.name,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${license.version} · ${license.licenseName}",
                    style = HaloType.body03Regular,
                    color = Gray500
                )
            }
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = licenseText,
                    style = HaloType.body03Regular,
                    color = Gray700
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "닫기",
                    color = Gray800
                )
            }
        }
    )
}
