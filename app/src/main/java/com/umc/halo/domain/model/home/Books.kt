package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color

data class Books(
    val id: Int,
    val color: Color,
    val title: String
)

data class CustomizedStoryBooks(
    val id: Int,
    val intro: String,
    val title: String,
    val subtitle: String
)