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
import com.umc.halo.presentation.theme.Gray700

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

    // currentStep이 1이면 첫 번째,
    // currentStep이 2이면 두 번째,
    // currentStep이 3이면 세 번째만 활성화된다.
    val activeIndex = safeCurrentStep - 1

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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalStep) { index ->
            val isActive = index == activeIndex

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(
                        color = if (isActive) {
                            Gray700
                        } else {
                            Gray100
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
            )
        }
    }
}