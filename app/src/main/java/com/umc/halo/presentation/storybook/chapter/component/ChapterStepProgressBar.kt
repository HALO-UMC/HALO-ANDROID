package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100

private val ActiveStepColor = Color(0xFF707070)

@Composable
fun ChapterStepProgressBar(
    currentStepIndex: Int,
    modifier: Modifier = Modifier,
    totalSteps: Int = 3
) {
    val segmentGap = 4.dp
    val safeCurrentIndex = currentStepIndex.coerceIn(
        minimumValue = 0,
        maximumValue = totalSteps - 1
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(5.dp)
    ) {
        val totalGapWidth = segmentGap * (totalSteps - 1)
        val segmentWidth = (maxWidth - totalGapWidth) / totalSteps

        Row {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .background(
                            color = if (index == safeCurrentIndex) {
                                ActiveStepColor
                            } else {
                                Gray100
                            },
                            shape = RoundedCornerShape(3.dp)
                        )
                )

                if (index != totalSteps - 1) {
                    Box(modifier = Modifier.width(segmentGap))
                }
            }
        }
    }
}