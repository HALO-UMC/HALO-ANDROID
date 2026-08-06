package com.umc.halo.presentation.navigation.main_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.umc.halo.presentation.calendar.CalendarScreen
import com.umc.halo.presentation.navigation.Graphs
import com.umc.halo.presentation.navigation.Routes
import com.umc.halo.presentation.storybook.chapter.ChapterResultRoute
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

fun NavGraphBuilder.calenderGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.CALENDAR,
        startDestination = Routes.CALENDAR
    ) {
        composable(Routes.CALENDAR) {
            CalendarScreen(
                // 캘린더 '바로 시작하기' / 빈 모달 '시작하기' → 스토리북. 항상 '전체' 탭에서 시작
                onNavigateToStorybookList = {
                    navController.clearBackStack(Graphs.STORYBOOK)
                    navController.navigate(Graphs.STORYBOOK) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Graphs.MAIN) { saveState = true }
                    }
                },
                // 캘린더 → 테마함(기본 위치)
                onNavigateToThemeBox = {
                    navController.navigate(Graphs.THEME_BOX) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Graphs.MAIN) { saveState = true }
                    }
                },
                // 모달 완성 스토리북 → 테마함의 그 스토리북
                onNavigateToThemeBoxStorybook = { storybookId ->
                    navController.navigate(Routes.themeBox(storybookId)) {
                        launchSingleTop = true
                        popUpTo(Graphs.MAIN) { saveState = true }
                    }
                },
                // 모달 '장 기록중' 카드 → 그 장의 완료 결과 화면
                onNavigateToChapterResult = { storybookId, chapterId ->
                    navController.navigate(Routes.chapterResult(storybookId, chapterId)) {
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
                    navController.popBackStackIfCurrent(Routes.CHAPTER_RESULT)
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
