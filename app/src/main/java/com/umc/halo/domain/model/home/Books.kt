package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color

data class Books(
    val id: Int,
    val color: Color,
    val title: String,
    val size: Size
)

data class Size(
    val width: Int,
    val height: Int
)