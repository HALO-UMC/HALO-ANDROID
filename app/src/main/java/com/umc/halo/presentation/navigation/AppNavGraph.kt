package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umc.halo.presentation.calendar.CalendarScreen
import com.umc.halo.presentation.home.HomeRoute
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.themebox.ThemeBoxRoute
import com.umc.halo.presentation.themebox.ThemeBoxScreen

// NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Route와 일치하는 화면 출력
    NavHost(
        navController = navController,

        // TODO: 온보딩 UI 확인용 임시 startDestination
        // 나중에 로그인 흐름 연결할 때 Routes.LOGIN 또는 Routes.SPLASH로 다시 변경 예정
        startDestination = Routes.HOME,

        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            Text(text = "Splash")
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginRoute()
        }

        composable(Routes.HOME) {
            HomeRoute(
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(
                // 캘린더 → 스토리북(전체탭) / 테마함
                // 하단바와 같은 백스택 옵션으로 전환
                onNavigateToStorybook = {
                    navController.navigate(Routes.STORYBOOK) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                onNavigateToThemeBox = {
                    navController.navigate(Routes.THEME_BOX) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                }
            )
        }

        composable(Routes.THEME_BOX) {
            ThemeBoxRoute(
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.STORYBOOK) {
            //스토리북
            StorybookScreen()
        }

        composable(Routes.MYPAGE) {
            Text(text = "MyPage")
        }

        composable(Routes.STORYBOOK_DETAIL) {
            StoryBookDetailScreen()
        }

        composable(Routes.CHAPTER_PROGRESS) {
            Text(text = "Chapter Progress")
        }

        composable(Routes.CHAPTER_RESULT) {
            Text(text = "Chapter Result")
        }
    }
}