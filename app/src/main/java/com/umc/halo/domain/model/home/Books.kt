package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 홈 화면 책장에서 쓰이는 책 정보
 */
data class Books(
    val id: Long,
    val spineImage: Int,
    val coverImage: Int,
    val height: Int,
    val width: Int,
    val tilt: Float,
) {
    val offsetX: Int
        get() = offsetXCalculate(width, height, tilt)

    val offsetY: Float
        get() = offsetYCalculate(width, height, tilt)
}

private fun offsetXCalculate(width: Int, height: Int, tilt: Float): Int {
    val rad = Math.toRadians(tilt.toDouble())

    return (abs(width * cos(rad)) + abs(height * sin(rad))).roundToInt()

}

private fun offsetYCalculate(
    width: Int,
    height: Int,
    tilt: Float
): Float {
    val rad = Math.toRadians(tilt.toDouble())

    val rotatedHeight =
        abs(width * sin(rad)) + abs(height * cos(rad))

    val rotationOffset =
        (height - rotatedHeight).toFloat() / 2f

    val extraOffset = if (tilt != 0f) 5f else 0f

    return rotationOffset + extraOffset
}

/**
 * 책장 클릭 시 띄워지는 '스토리북으로 이동하기' 정보
 */
data class StartStorybook(
    val storybookId: Long = 0,
    val title: String = "void",
    val tag: String? = "void", // 상단 태그
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false,
    val isFirst: Boolean = false
)