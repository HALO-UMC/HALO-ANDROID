package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Books(
    val id: Int,
    val title: String,
    val subtitle: String,
    val spineImage: Int,
    val coverImage: Int,
    val height: Int,
    val width: Int,
    val tilt: Float,
    val offsetY: Float,
    val offsetX: Int
)

data class CustomizedStoryBooks(
    val id: Int,
    val intro: String,
    val title: String,
    val subtitle: String
)