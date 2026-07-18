package com.umc.halo.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Primary500

@Composable
fun OnboardingProgressBar(
    currentStep: Int,
    totalStep: Int,
    modifier: Modifier = Modifier
) {
    if (totalStep <= 0) return

    val safeCurrentStep = currentStep.coerceIn(
        minimumValue = 0,
        maximumValue = totalStep
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = safeCurrentStep.toFloat(),
                    range = 0f..totalStep.toFloat(),
                    steps = (totalStep - 1).coerceAtLeast(0)
                )
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalStep) { index ->
            val isActive = index < safeCurrentStep

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (isActive) {
                            Primary500
                        } else {
                            Gray100
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
            )
        }
    }
}