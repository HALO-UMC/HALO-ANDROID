package com.umc.halo.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.calendar.CalendarScreen
import kotlin.math.round

fun NavGraphBuilder.calenderGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.CALENDAR,
        startDestination = Routes.CALENDAR
    ) {
        composable(Routes.CALENDAR) {
            CalendarScreen(
                // 캘린더 → 스토리북(전체탭) / 테마함
                // 하단바와 같은 백스택 옵션으로 전환
                onNavigateToStorybookList = {
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
                },
                // 모달 '장 기록중' 카드 → 그 장의 완료 결과 화면
                onNavigateToChapterResult = { storybookId, chapterId ->
                    navController.navigate(Routes.chapterResult(storybookId, chapterId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}