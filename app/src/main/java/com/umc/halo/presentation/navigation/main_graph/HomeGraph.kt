package com.umc.halo.presentation.navigation.main_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.umc.halo.presentation.home.HomeRoute
import com.umc.halo.presentation.navigation.Graphs
import com.umc.halo.presentation.navigation.Routes
import com.umc.halo.presentation.storybook.chapter.ChapterProgressRoute
import com.umc.halo.presentation.storybook.chapter.ChapterResultRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailRoute
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.HOME,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeRoute(
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.STORYBOOK_DETAIL) {
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

        composable(Routes.SHOW_THEME) {
            ShowThemeRoute(
                onNavigateBack = {
                    navController.popBackStackIfCurrent(Routes.SHOW_THEME)
                }
            )
        }
    }
}
