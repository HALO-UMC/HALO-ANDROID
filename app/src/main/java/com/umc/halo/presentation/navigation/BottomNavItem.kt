package com.umc.halo.presentation.navigation

import androidx.annotation.DrawableRes
//import androidx.compose.ui.graphics.vector.ImageVector
import com.umc.halo.R


//하단바 메뉴 이름, route, 아이콘 정보 담당
enum class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    HOME(
        route = Graphs.HOME,
        label = "홈",
        selectedIcon = R.drawable.ic_bottomnav_home_filled,
        unselectedIcon = R.drawable.ic_bottomnav_home_outlined
    ),
    CALENDAR(
        route = Graphs.CALENDAR,
        label = "캘린더",
        selectedIcon = R.drawable.ic_bottomnav_calendar_filled,
        unselectedIcon = R.drawable.ic_bottomnav_calendar_outlined
    ),
    STORYBOOK(
        route = Graphs.STORYBOOK,
        label = "스토리북",
        selectedIcon = R.drawable.ic_bottomnav_storybook_filled,
        unselectedIcon = R.drawable.ic_bottomnav_storybook_outlined
    ),
    THEME_BOX(
        route = Graphs.THEME_BOX,
        label = "테마함",
        selectedIcon = R.drawable.ic_bottomnav_theme_filled,
        unselectedIcon = R.drawable.ic_bottomnav_theme_outlined
    ),
    MYPAGE(
        route = Graphs.MYPAGE,
        label = "마이페이지",
        selectedIcon = R.drawable.ic_bottomnav_mypage_filled,
        unselectedIcon = R.drawable.ic_bottomnav_mypage_outlined
    )
}