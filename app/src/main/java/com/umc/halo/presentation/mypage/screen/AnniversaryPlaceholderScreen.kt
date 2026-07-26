package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType

@Composable
fun AnniversaryPlaceholderScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "기념일 관리", onBack = onBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "기념일 관리 화면은 다음 단계에서 이어서 구현할게요.",
                style = HaloType.body02Medium.copy(fontSize = 15.sp),
                color = Gray500,
                textAlign = TextAlign.Center
            )
        }
    }
}
