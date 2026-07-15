package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 온보딩 관계 태그 선택 카드 컨포넌트
@Composable
fun OnboardingSelectCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Primary50 else White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary500 else Gray200
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
        ) {
            Text(
                text = title,
                style = HaloType.body01SemiBold,
                color = if (selected) Primary500 else Gray800
            )

            if (description != null) {
                Text(
                    text = description,
                    style = HaloType.body02Medium,
                    color = Gray500,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}