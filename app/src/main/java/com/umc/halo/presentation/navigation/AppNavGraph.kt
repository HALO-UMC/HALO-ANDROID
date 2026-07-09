package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


//NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    //--Route와 일치하는 화면 출력
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
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
            //홈화면
            Text(text = "Home")
        }

        composable(Routes.CALENDAR) {
            //캘린더
            Text(text = "Calendar")
        }

        composable(Routes.THEME_BOX) {
            //테마함
            Text(text = "Theme Box")
        }

        composable(Routes.STORYBOOK) {
            //스토리북
            Text(text = "Storybook")
        }

        composable(Routes.MYPAGE) {
            //마이페이지
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

