package com.umc.halo.presentation.navigation

enum class BottomNavItem(
    val route: String,
    val label: String
) {
    HOME(
        route = Routes.HOME,
        label = "홈"
    ),
    CALENDAR(
        route = Routes.CALENDAR,
        label = "캘린더"
    ),
    THEME_BOX(
        route = Routes.THEME_BOX,
        label = "테마함"
    ),
    STORYBOOK(
        route = Routes.STORYBOOK,
        label = "스토리북"
    ),
    MYPAGE(
        route = Routes.MYPAGE,
        label = "마이페이지"
    )
}