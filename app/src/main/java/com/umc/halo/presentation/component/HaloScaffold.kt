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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umc.halo.presentation.navigation.BottomNavItem
import com.umc.halo.presentation.navigation.Routes
import com.umc.halo.presentation.storybook.detail.StoryBookDetailTopBar

@Composable
fun HaloScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = BottomNavItem.entries.map { it.route }

    /*
     * 챕터 화면은 배경 이미지가 상태바 뒤까지 이어지는
     * Edge-to-Edge 화면으로 구성합니다.
     *
     * 이 화면들에서는 Scaffold가 상태바 여백을 자동으로
     * 추가하지 않도록 설정합니다.
     */
    val isChapterEdgeToEdgeRoute =
        currentRoute == Routes.CHAPTER_PROGRESS ||
                currentRoute == Routes.CHAPTER_RESULT

    Scaffold(
        contentWindowInsets = if (isChapterEdgeToEdgeRoute) {
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
            when (currentRoute) {
                Routes.HOME -> {
                    HaloTopBar(
                        title = "HALO",
                        showLeftIcon = false
                    )
                }

                // Routes.MYPAGE -> MyPageTopBar()
                // Routes.CALENDAR -> CalendarTopBar()

                Routes.THEME_BOX -> {
                    HaloTopBar(
                        title = "테마전시관",
                        showLeftIcon = false
                    )
                }

                Routes.STORYBOOK -> {
                    HaloTopBar(
                        title = "HALO",
                        showLeftIcon = false
                    )
                }

                Routes.STORYBOOK_DETAIL -> {
                    StoryBookDetailTopBar()
                }
            }
        },

        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
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
                        currentRoute = currentRoute
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}