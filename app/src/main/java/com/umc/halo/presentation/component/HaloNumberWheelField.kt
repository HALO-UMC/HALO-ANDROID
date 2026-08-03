package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HaloNumberWheelField(
    selectedValue: Int?,
    values: List<Int>,
    placeholder: String,
    unit: String,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueFormatter: (Int) -> String = { it.toString() },
    fieldHeight: Dp = 68.dp,
    horizontalPadding: Dp = 24.dp,
    cornerRadius: Dp = 16.dp,
    usePlaceholder: Boolean = true,
    circular: Boolean = false
) {
    val wheelValues = remember(
        values,
        usePlaceholder
    ) {
        if (usePlaceholder) {
            listOf<Int?>(null) + values
        } else {
            values
        }
    }
    val displayItemCount = if (circular && wheelValues.isNotEmpty()) {
        Int.MAX_VALUE
    } else {
        wheelValues.size
    }
    val selectedIndex = remember(
        selectedValue,
        wheelValues,
        circular
    ) {
        val baseIndex = selectedValue
            ?.let { wheelValues.indexOf(it) }
            ?.takeIf { it >= 0 }
            ?: 0

        if (circular && wheelValues.isNotEmpty()) {
            val middleLoopStart = Int.MAX_VALUE / 2 -
                    (Int.MAX_VALUE / 2 % wheelValues.size)
            middleLoopStart + baseIndex
        } else {
            baseIndex
        }
    }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val itemHeightPx = with(LocalDensity.current) {
        fieldHeight.toPx()
    }
    val isScrolling by remember {
        derivedStateOf { listState.isScrollInProgress }
    }
    var hasScrollStarted by remember { mutableStateOf(false) }

    LaunchedEffect(selectedIndex) {
        if (!listState.isScrollInProgress &&
            listState.firstVisibleItemIndex != selectedIndex
        ) {
            listState.scrollToItem(selectedIndex)
        }
    }

    LaunchedEffect(
        listState,
        wheelValues
    ) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (scrolling) {
                    hasScrollStarted = true
                } else if (hasScrollStarted) {
                    val shouldAdvance = listState.firstVisibleItemScrollOffset > itemHeightPx / 2
                    val targetIndex = (
                            listState.firstVisibleItemIndex +
                                    if (shouldAdvance) 1 else 0
                            ).coerceIn(0, displayItemCount - 1)

                    if (
                        listState.firstVisibleItemIndex != targetIndex ||
                        listState.firstVisibleItemScrollOffset != 0
                    ) {
                        listState.animateScrollToItem(targetIndex)
                    }

                    val valueIndex = targetIndex.toWheelValueIndex(wheelValues.size)
                    wheelValues[valueIndex]?.let(onValueSelected)
                    hasScrollStarted = false
                }
            }
    }

    Box(
        modifier = modifier
            .height(fieldHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Gray30),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                items(displayItemCount) { index ->
                    val value = wheelValues[index.toWheelValueIndex(wheelValues.size)]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(fieldHeight),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = value?.let(valueFormatter) ?: placeholder,
                            style = HaloType.body02Medium,
                            color = when {
                                value == null -> Gray300
                                isScrolling -> Gray300
                                else -> Gray800
                            }
                        )
                    }
                }
            }

            Text(
                text = unit,
                style = HaloType.body02Medium,
                color = Gray800
            )
        }
    }
}

private fun Int.toWheelValueIndex(valueCount: Int): Int {
    if (valueCount <= 0) return 0
    return floorMod(this, valueCount)
}

private fun floorMod(value: Int, divisor: Int): Int {
    val remainder = value % divisor
    return if (remainder < 0) remainder + divisor else remainder
}
