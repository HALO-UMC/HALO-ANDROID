package com.umc.halo.presentation.home

import androidx.compose.runtime.Composable
import com.umc.halo.presentation.component.HaloTopBar

@Composable
fun HomeTopBar() {
    HaloTopBar(
        title = "HALO",
        showLeftIcon = false
    )
}