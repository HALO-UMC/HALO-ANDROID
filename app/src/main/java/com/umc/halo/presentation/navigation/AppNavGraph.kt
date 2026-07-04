package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            Text(text = "Splash")
        }

        composable(Routes.ONBOARDING) {
            Text(text = "Onboarding")
        }

        composable(Routes.LOGIN) {
            Text(text = "Login")
        }

        composable(Routes.HOME) {
            Text(text = "Home")
        }

        composable(Routes.CALENDAR) {
            Text(text = "Calendar")
        }

        composable(Routes.THEME_BOX) {
            Text(text = "Theme Box")
        }

        composable(Routes.STORYBOOK) {
            Text(text = "Storybook")
        }

        composable(Routes.MYPAGE) {
            Text(text = "MyPage")
        }

        composable(Routes.STORYBOOK_DETAIL) {
            Text(text = "Storybook Detail")
        }

        composable(Routes.CHAPTER_PROGRESS) {
            Text(text = "Chapter Progress")
        }

        composable(Routes.CHAPTER_RESULT) {
            Text(text = "Chapter Result")
        }
    }
}