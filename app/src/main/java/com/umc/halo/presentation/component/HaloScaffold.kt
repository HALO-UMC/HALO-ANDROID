package com.umc.halo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umc.halo.presentation.calendar.CalendarTopBar
import com.umc.halo.presentation.navigation.BottomNavItem
import com.umc.halo.presentation.navigation.Routes
import com.umc.halo.presentation.storybook.detail.StoryBookDetailTopBar
import com.umc.halo.presentation.themebox.show_theme.ShowThemeTopBar
import okhttp3.Route

@Composable
fun HaloScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavRoutes = BottomNavItem.entries.map { it.route }

    val graphRoute = currentDestination?.hierarchy
        ?.firstOrNull { it.route in bottomNavRoutes }
        ?.route

    val screenRoute = currentDestination?.route

    val hideBottomBarRoutes = listOf(
        Routes.STORYBOOK_DETAIL,
        Routes.CHAPTER_PROGRESS,
        Routes.CHAPTER_RESULT,
        Routes.SHOW_THEME
    )

    val showBottomBar =
        graphRoute != null && screenRoute !in hideBottomBarRoutes

    /*
     * 챕터 화면은 Edge-to-Edge로 구성하고, 온보딩 화면은
     * 각 단계에서 시스템 바 여백을 직접 관리합니다.
     */
    val isEdgeToEdgeRoute = screenRoute == Routes.CHAPTER_PROGRESS ||
                            screenRoute == Routes.CHAPTER_RESULT ||
                            screenRoute == Routes.ONBOARDING ||
                            screenRoute == Routes.TERMS ||
                            screenRoute == Routes.SHOW_THEME ||
                            screenRoute == Routes.SPLASH

    Scaffold(
        contentWindowInsets = if (isEdgeToEdgeRoute) {
            WindowInsets(
                left = 0,
                top = 0,
                right = 0,
                bottom = 0
            )
        } else {
            ScaffoldDefaults.contentWindowInsets
        },

        topBar = {
            when(screenRoute) {
                Routes.HOME -> HaloTopBar(title = "HALO", showLeftIcon = false)
                //Routes.MYPAGE -> MyPageTopBar()
                Routes.CALENDAR -> CalendarTopBar()
                Routes.THEME_BOX -> HaloTopBar(title = "테마함", showLeftIcon = false)
                Routes.STORYBOOK -> HaloTopBar(title = "HALO", showLeftIcon = false)
                Routes.STORYBOOK_DETAIL -> StoryBookDetailTopBar() {navController.popBackStack()}
            }
        },

        bottomBar = {
            if (showBottomBar) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF858585).copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )

                    BottomNav(
                        navController = navController,
                        currentRoute = graphRoute
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
