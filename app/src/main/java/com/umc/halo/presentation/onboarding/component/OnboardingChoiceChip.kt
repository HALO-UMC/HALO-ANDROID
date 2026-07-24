package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500

/**
 * 온보딩 부모님 성격 태그 선택 컴포넌트
 */
@Composable
fun OnboardingChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier
            .height(30.dp)
            .toggleable(
                value = selected,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = {
                    onClick()
                }
            ),
        shape = RoundedCornerShape(100.dp),
        color = if (selected) {
            Primary50
        } else {
            Gray50
        },
        border = if (selected) {
            BorderStroke(
                width = 1.dp,
                color = Primary500
            )
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = HaloType.body03Regular.copy(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp
                ),
                color = if (selected) {
                    Primary500
                } else {
                    Gray400
                }
            )
        }
    }
}