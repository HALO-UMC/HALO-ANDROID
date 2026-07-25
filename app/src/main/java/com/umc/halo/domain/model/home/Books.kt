package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color

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
    val offsetX: Int,
    val currentProgress: Int,
    val isCompleted: Boolean
)

data class CustomizedStoryBooks(
    val id: Int,
    val intro: String,
    val title: String,
    val subtitle: String
)

data class StartStorybook(
    val storybookId: Int = 0,
    val title: String = "void",
    val subtitle: String = "void",
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false
)