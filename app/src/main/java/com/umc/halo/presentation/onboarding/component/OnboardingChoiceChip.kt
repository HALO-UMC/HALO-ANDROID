package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 온보딩 성격 태그 선택 컴포넌트
@Composable
fun OnboardingChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.Checkbox,
            onClick = onClick
        ),
        shape = RoundedCornerShape(30.dp),
        color = if (selected) Primary50 else White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary500 else Gray200
        )
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium,
            color = if (selected) Primary500 else Gray700,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
        )
    }
}