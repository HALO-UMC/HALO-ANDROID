package com.umc.halo.presentation.navigation.main_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.home.HomeRoute
import com.umc.halo.presentation.navigation.Graphs
import com.umc.halo.presentation.navigation.Routes

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

        }

        composable(Routes.CHAPTER_PROGRESS) {

        }

        composable(Routes.CHAPTER_RESULT) {

        }

        composable(Routes.THEME_BOX) {

        }
    }
}