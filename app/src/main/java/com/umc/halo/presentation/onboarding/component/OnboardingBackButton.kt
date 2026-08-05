package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray800

@Composable
fun OnboardingBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(44.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_common_chevron_left),
            contentDescription = "이전 화면",
            tint = Gray800,
            modifier = Modifier.size(8.dp, 12.dp)
        )
    }
}
