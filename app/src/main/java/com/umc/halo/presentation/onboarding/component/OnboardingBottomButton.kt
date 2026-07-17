package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 온보딩 화면 아래의 다음, 시작하기 버튼 컴포넌트
@Composable
fun OnboardingBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = White,
            disabledContainerColor = Gray100,
            disabledContentColor = Gray400
        )
    ) {
        Text(
            text = text,
            style = HaloType.body01SemiBold.copy(
                lineHeight = 23.2.sp,
                letterSpacing = (-0.16).sp
            )
        )
    }
}