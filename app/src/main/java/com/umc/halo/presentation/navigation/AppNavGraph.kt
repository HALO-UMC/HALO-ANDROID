package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.storybook.chapter.ChapterResultRoute
import com.umc.halo.presentation.storybook.chapter.ChapterProgressRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.themebox.ThemeBoxScreen

// NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,

        /*
         * 챕터 첫 화면 UI 확인용 임시 startDestination
         *
         * 첫 화면 확인이 끝나면 다시 Routes.HOME으로 복구할 예정입니다.
         */
        startDestination = Routes.chapterProgress(
            storybookId = 1L,
            chapterId = 1L
        ),

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
            ThemeBoxScreen()
        }

        composable(Routes.STORYBOOK) {
            StorybookScreen()
        }

        composable(Routes.MYPAGE) {
            Text(text = "MyPage")
        }

        composable(Routes.STORYBOOK_DETAIL) {
            StoryBookDetailScreen()
        }

        composable(
            route = Routes.CHAPTER_PROGRESS,
            arguments = listOf(
                navArgument("storybookId") {
                    type = NavType.LongType
                },
                navArgument("chapterId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val storybookId = backStackEntry.arguments
                ?.getLong("storybookId")
                ?: return@composable

            val chapterId = backStackEntry.arguments
                ?.getLong("chapterId")
                ?: return@composable

            ChapterProgressRoute(
                storybookId = storybookId,
                chapterId = chapterId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToResult = { resultStorybookId, resultChapterId ->
                    navController.navigate(
                        Routes.chapterResult(
                            storybookId = resultStorybookId,
                            chapterId = resultChapterId
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.CHAPTER_RESULT,
            arguments = listOf(
                navArgument("storybookId") {
                    type = NavType.LongType
                },
                navArgument("chapterId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val storybookId = backStackEntry.arguments
                ?.getLong("storybookId")
                ?: return@composable

            val chapterId = backStackEntry.arguments
                ?.getLong("chapterId")
                ?: return@composable

            ChapterResultRoute(
                storybookId = storybookId,
                chapterId = chapterId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
