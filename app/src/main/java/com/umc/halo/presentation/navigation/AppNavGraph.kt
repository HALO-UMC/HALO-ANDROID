package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umc.halo.presentation.calendar.CalendarScreen
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.terms.TermsRoute
import com.umc.halo.presentation.themebox.ThemeBoxScreen

// NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Route와 일치하는 화면 출력
    NavHost(
        navController = navController,

        // TODO: 테스트용 화면 시작점 = LOGIN
        // 실제 앱 시작점은 추후 Routes.SPLASH 로 변경 예정
        startDestination = Routes.LOGIN,

        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            Text(text = "Splash")
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            // 로그인 성공 → 약관동의
            // 약관 상단바 뒤로가기로 로그인에 돌아올 수 있게 로그인 화면을 백스택에 남겨둠
            LoginRoute(
                onNavigateToTerms = { navController.navigate(Routes.TERMS) }
            )
        }

        composable(Routes.TERMS) {
            TermsRoute(
                // 상단바 뒤로가기 → 이전 화면(로그인)
                onBack = { navController.popBackStack() },
                // '다음'(약관 동의 완료) → 온보딩
                // 뒤로가기로 버튼으로 약관 화면에 못 돌아오게 TERMS 제거
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.TERMS) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(
                // 캘린더 → 스토리북(전체탭) / 테마함
                // 하단바와 같은 백스택 옵션으로 전환
                onNavigateToStorybook = {
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
                }
            )
        }

        composable(Routes.THEME_BOX) {
            ThemeBoxScreen()
        }

        composable(Routes.STORYBOOK) {
            //스토리북
            StorybookScreen()
        }

        composable(Routes.MYPAGE) {
            Text(text = "MyPage")
        }

        composable(Routes.STORYBOOK_DETAIL) {
            StoryBookDetailScreen()
        }

        composable(Routes.CHAPTER_PROGRESS) {
            Text(text = "Chapter Progress")
        }

        composable(Routes.CHAPTER_RESULT) {
            Text(text = "Chapter Result")
        }
    }
}