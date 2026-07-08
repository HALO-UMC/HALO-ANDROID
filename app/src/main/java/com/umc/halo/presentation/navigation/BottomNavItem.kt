package com.umc.halo.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.umc.halo.R

enum class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    HOME(
        route = Routes.HOME,
        label = "홈",
        selectedIcon = R.drawable.ic_bottomnav_home_filled,
        unselectedIcon = R.drawable.ic_bottomnav_home_outlined
    ),
    CALENDAR(
        route = Routes.CALENDAR,
        label = "캘린더",
        selectedIcon = R.drawable.ic_bottomnav_calendar_filled,
        unselectedIcon = R.drawable.ic_bottomnav_calendar_outlined
    ),
    THEME_BOX(
        route = Routes.THEME_BOX,
        label = "테마함",
        selectedIcon = R.drawable.ic_bottomnav_theme_filled,
        unselectedIcon = R.drawable.ic_bottomnav_theme_outlined
    ),
    STORYBOOK(
        route = Routes.STORYBOOK,
        label = "스토리북",
        selectedIcon = R.drawable.ic_bottomnav_storybook_filled,
        unselectedIcon = R.drawable.ic_bottomnav_storybook_outlined
    ),
    MYPAGE(
        route = Routes.MYPAGE,
        label = "마이페이지",
        selectedIcon = R.drawable.ic_bottomnav_mypage_filled,
        unselectedIcon = R.drawable.ic_bottomnav_mypage_outlined
    )
}