package com.umc.halo.presentation.component

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umc.halo.presentation.calendar.CalendarTopBar
import com.umc.halo.presentation.mypage.component.ConfirmActionDialog
import com.umc.halo.presentation.navigation.BottomNavItem
import com.umc.halo.presentation.navigation.Routes

@Composable
fun HaloScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val activity = LocalActivity.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavRoutes = BottomNavItem.entries.map { it.route }

    val graphRoute = currentDestination?.hierarchy
        ?.firstOrNull { it.route in bottomNavRoutes }
        ?.route

    val screenRoute = currentDestination?.route

    val mainRootRoutes = listOf(
        Routes.HOME,
        Routes.CALENDAR,
        Routes.STORYBOOK,
        Routes.THEME_BOX,
        Routes.MYPAGE
    )

    val showBottomBar =
        graphRoute != null && screenRoute in mainRootRoutes

    /*
     * 시스템 바의 뒤로가기로 '앱 종료'를 물어볼 화면
     * 탭 최상위 화면 + 로그인 흐름(로그인·재로그인·약관동의)에 적용됨
     */
    val exitOnBackRoutes = mainRootRoutes + listOf(Routes.LOGIN, Routes.RELOGIN, Routes.TERMS)

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = screenRoute in exitOnBackRoutes) {
        showExitDialog = true
    }

    if (showExitDialog) {
        ConfirmActionDialog(
            title = "앱을 종료하시겠습니까?",
            description = "확인을 누르면 HALO가 종료됩니다.",
            buttonText = "종료",
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                activity?.finish()
            }
        )
    }

    /*
     * 챕터 화면은 Edge-to-Edge로 구성하고, 온보딩 화면은
     * 각 단계에서 시스템 바 여백을 직접 관리합니다.
     */
    val isEdgeToEdgeRoute = screenRoute == Routes.CHAPTER_PROGRESS ||
                            screenRoute == Routes.CHAPTER_RESULT ||
                            screenRoute == Routes.ONBOARDING ||
                            screenRoute == Routes.TERMS ||
                            screenRoute == Routes.SHOW_THEME ||
                            screenRoute == Routes.SPLASH ||
                            screenRoute == Routes.STORYBOOK_DETAIL

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
