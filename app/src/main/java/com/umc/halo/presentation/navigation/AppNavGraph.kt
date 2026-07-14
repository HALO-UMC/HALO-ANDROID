package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.storybook.index.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen

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
        startDestination = Routes.STORYBOOK_DETAIL,

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
            HomeScreen()
        }

        composable(Routes.CALENDAR) {
            Text(text = "Calendar")
        }

        composable(Routes.THEME_BOX) {
            Text(text = "Theme Box")
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