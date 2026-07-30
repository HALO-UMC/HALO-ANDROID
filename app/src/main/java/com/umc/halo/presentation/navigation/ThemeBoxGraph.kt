package com.umc.halo.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.themebox.ThemeBoxRoute
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

fun NavGraphBuilder.themeBoxGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.STORYBOOK,
        startDestination = Routes.STORYBOOK
    ) {
        composable(Routes.THEME_BOX) {
            ThemeBoxRoute(
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SHOW_THEME) {
            ShowThemeRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}