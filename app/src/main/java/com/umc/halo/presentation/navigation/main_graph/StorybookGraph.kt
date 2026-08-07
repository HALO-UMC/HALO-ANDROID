package com.umc.halo.presentation.navigation.main_graph

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.umc.halo.presentation.navigation.Graphs
import com.umc.halo.presentation.navigation.Routes
import com.umc.halo.presentation.storybook.chapter.ChapterProgressRoute
import com.umc.halo.presentation.storybook.chapter.ChapterResultRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailRoute
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

fun NavGraphBuilder.storybookGraph(
    navController: NavController
) {
    Log.d("NAV", "storybookGraph registered")

    navigation(
        route = Graphs.STORYBOOK,
        startDestination = Routes.STORYBOOK
    ) {
        composable(Routes.STORYBOOK) {
            StorybookScreen(
                // 맞춤카드 및 시작전, 진행중 스토리북 카드 -> 스토리북 상세(목차)
                onNavigateToStorybookDetail = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                },
                // 완료 카드 -> 테마함의 그 스토리북
                onNavigateToThemeBox = { storybookId ->
                    navController.navigate(Routes.themeBox(storybookId)) {
                        launchSingleTop = true
                        popUpTo(Graphs.MAIN) { saveState = true }
                    }
                }
            )
        }

        composable(
            route = Routes.STORYBOOK_DETAIL,
            arguments = listOf(
                navArgument("storybookId") {
                    type = NavType.LongType
                }
            )
        ) {
            StoryBookDetailRoute(
                onNavigateToChapterResult = { storybookId, chapterId ->
                    navController.navigate(Routes.chapterResult(storybookId,chapterId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChapterProgress = { storybookId, chapterId ->
                    navController.navigate(Routes.chapterProgress(storybookId,chapterId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToShowTheme = { storybookId ->
                    navController.navigate(Routes.showTheme(storybookId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToBack = {
                    navController.popBackStackIfCurrent(Routes.STORYBOOK_DETAIL)
                }
            )
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
                    navController.popBackStackIfCurrent(Routes.CHAPTER_PROGRESS)
                },
                onNavigateToResult = { resultStorybookId, _ ->
                    navController.navigate(
                        Routes.storybookDetail(resultStorybookId)
                    ) {
                        popUpTo(Routes.storybookDetail(resultStorybookId)) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
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
                    navController.navigateIfCurrent(Routes.CHAPTER_RESULT) {
                        navigate(Routes.storybookDetail(storybookId)) {
                            popUpTo(Routes.storybookDetail(storybookId)) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.SHOW_THEME,
            arguments = listOf(
                navArgument("storybookId") { type = NavType.LongType }
            )
        ) {
            ShowThemeRoute(
                onNavigateBack = { navController.popBackStackIfCurrent(Routes.SHOW_THEME) }
            )
        }
    }
}
