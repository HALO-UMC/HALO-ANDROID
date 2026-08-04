package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary600

// 온보딩 관계 선택 카드 컴포넌트
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
            .defaultMinSize(minHeight = if (description == null) 52.dp else 0.dp)
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) Primary50 else Gray30
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Text(
                text = title,
                style = HaloType.body02Medium,
                color = if (selected) Primary600 else Gray700
            )

            if (description != null) {
                Text(
                    text = description,
                    style = HaloType.caption01Regular,
                    color = if (selected) Primary400 else Gray500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
