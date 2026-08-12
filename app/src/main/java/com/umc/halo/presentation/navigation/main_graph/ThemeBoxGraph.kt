package com.umc.halo.presentation.navigation.main_graph

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
import com.umc.halo.presentation.themebox.ThemeBoxRoute
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

fun NavGraphBuilder.themeBoxGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.THEME_BOX,
        startDestination = Routes.THEME_BOX
    ) {
        composable(
            route = Routes.THEME_BOX,
            arguments = listOf(
                navArgument("storybookId") {
                    type = NavType.LongType
                    // 선택 인자라 기본값이 필요
                    defaultValue = Routes.NO_STORYBOOK_ID
                }
            )
        ) { backStackEntry ->
            // 기본값(-1)이면 '지정 없음' → 첫 번째 테마부터
            val initialStorybookId = backStackEntry.arguments
                ?.getLong("storybookId")
                ?.takeIf { it != Routes.NO_STORYBOOK_ID }

            ThemeBoxRoute(
                initialStorybookId = initialStorybookId,
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToShowTheme = { storybookId ->
                    navController.navigate(Routes.showTheme(storybookId)) {
                        launchSingleTop = true
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
                onNavigateBack = { storybookId ->
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
                onNavigateToChapterResult = { memberChapterId ->
                    navController.navigate(Routes.chapterResult(memberChapterId)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChapterProgress = { storybookId, chapterOrder ->
                    navController.navigate(Routes.chapterProgress(storybookId,chapterOrder)) {
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
                navArgument("chapterOrder") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val storybookId = backStackEntry.arguments
                ?.getLong("storybookId")
                ?: return@composable

            val chapterOrder = backStackEntry.arguments
                ?.getInt("chapterOrder")
                ?: return@composable

            ChapterProgressRoute(
                storybookId = storybookId,
                chapterOrder = chapterOrder,
                onNavigateBack = {
                    navController.popBackStackIfCurrent(Routes.CHAPTER_PROGRESS)
                },
                onNavigateToStorybookDetail = { resultStorybookId ->
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
                navArgument("memberChapterId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val memberChapterId = backStackEntry.arguments
                ?.getLong("memberChapterId")
                ?: return@composable

            ChapterResultRoute(
                memberChapterId = memberChapterId,
                onNavigateBack = {
                    navController.popBackStackIfCurrent(Routes.CHAPTER_RESULT)
                }
            )
        }
    }
}
