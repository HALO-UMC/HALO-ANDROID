package com.umc.halo.presentation.calendar

import androidx.compose.runtime.Composable
import com.umc.halo.presentation.component.HaloTopBar

/** 캘린더 상단바  */
@Composable
fun CalendarTopBar() {
    HaloTopBar(
        title = "기록",
        showLeftIcon = false
    )
}
