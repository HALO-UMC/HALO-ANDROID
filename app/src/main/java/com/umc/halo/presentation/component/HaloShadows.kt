package com.umc.halo.presentation.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private val CardShadowColor = Color(0xCFECE9E7)
private val BookShadowColor = Color(0x33999999)

/**
 * Card_shadow
 * box-shadow: 0 0 4px 2px rgba(236, 233, 231, 0.81)
 */
fun Modifier.haloCardShadow(shape: Shape): Modifier =
    shadow(
        elevation = 4.dp,
        shape = shape,
        clip = false,
        ambientColor = CardShadowColor,
        spotColor = CardShadowColor
    )

/**
 * Book_shadow
 * box-shadow: 4px 2px 8px 0 rgba(153, 153, 153, 0.20)
 */
fun Modifier.haloBookShadow(shape: Shape): Modifier =
    shadow(
        elevation = 8.dp,
        shape = shape,
        clip = false,
        ambientColor = BookShadowColor,
        spotColor = BookShadowColor
    )
