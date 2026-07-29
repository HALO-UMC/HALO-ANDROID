package com.umc.halo.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.home.HomeRoute

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
    }
}