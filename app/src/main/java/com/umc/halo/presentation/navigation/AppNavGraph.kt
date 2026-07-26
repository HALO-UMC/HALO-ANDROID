package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umc.halo.presentation.storybook.chapter.ChapterResultRoute
import com.umc.halo.presentation.storybook.chapter.ChapterProgressRoute
import com.umc.halo.presentation.calendar.CalendarScreen
import com.umc.halo.presentation.home.HomeRoute
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.mypage.AccountInfoRoute
import com.umc.halo.presentation.mypage.AnniversaryRoute
import com.umc.halo.presentation.mypage.NotificationSettingsRoute
import com.umc.halo.presentation.mypage.SystemSettingsRoute
import com.umc.halo.presentation.mypage.WithdrawRoute
import com.umc.halo.presentation.mypage.screen.AccountManagementScreen
import com.umc.halo.presentation.mypage.screen.MyPageScreen
import com.umc.halo.presentation.mypage.screen.OpenLicenseScreen
import com.umc.halo.presentation.mypage.screen.PrivacyPolicyScreen
import com.umc.halo.presentation.mypage.screen.RelationshipInfoScreen
import com.umc.halo.presentation.mypage.screen.TermsScreen
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.splash.SplashRoute
import com.umc.halo.presentation.splash.SplashScreen
import com.umc.halo.presentation.storybook.detail.StoryBookDetailRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.terms.TermsRoute
import com.umc.halo.presentation.themebox.ThemeBoxRoute
import com.umc.halo.presentation.themebox.ThemeBoxScreen
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

// NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,

        // TODO: 테스트용 화면 시작점 = LOGIN
        // 실제 앱 시작점은 추후 Routes.SPLASH 로 변경 예정
        startDestination = Routes.HOME,

        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            SplashRoute(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
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
            HomeRoute(
                onNavigateToStorybook = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                }
            )
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

        composable(Routes.STORYBOOK) {
            StorybookScreen()
        }

        composable(Routes.MYPAGE) {
            MyPageScreen(
                onNavigateToRelationshipInfo = {
                    navController.navigate(Routes.MYPAGE_RELATIONSHIP_INFO) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAnniversary = {
                    navController.navigate(Routes.MYPAGE_ANNIVERSARY) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSystemSettings = {
                    navController.navigate(Routes.MYPAGE_SYSTEM_SETTINGS) {
                        launchSingleTop = true
                    }
                },
                onNavigateToNotificationSettings = {
                    navController.navigate(Routes.MYPAGE_NOTIFICATION_SETTINGS) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAccountManagement = {
                    navController.navigate(Routes.MYPAGE_ACCOUNT_MANAGEMENT) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MYPAGE_RELATIONSHIP_INFO) {
            RelationshipInfoScreen(onBack = navController::popBackStack)
        }

        composable(Routes.MYPAGE_ANNIVERSARY) {
            AnniversaryRoute(onBack = navController::popBackStack)
        }

        composable(Routes.MYPAGE_SYSTEM_SETTINGS) {
            SystemSettingsRoute(onBack = navController::popBackStack)
        }

        composable(Routes.MYPAGE_NOTIFICATION_SETTINGS) {
            NotificationSettingsRoute(onBack = navController::popBackStack)
        }

        composable(Routes.MYPAGE_ACCOUNT_MANAGEMENT) {
            AccountManagementScreen(
                onBack = navController::popBackStack,
                onNavigateToAccountInfo = {
                    navController.navigate(Routes.MYPAGE_ACCOUNT_INFO) {
                        launchSingleTop = true
                    }
                },
                onNavigateToOpenLicense = {
                    navController.navigate(Routes.MYPAGE_OPEN_LICENSE) {
                        launchSingleTop = true
                    }
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Routes.MYPAGE_PRIVACY_POLICY) {
                        launchSingleTop = true
                    }
                },
                onNavigateToTerms = {
                    navController.navigate(Routes.MYPAGE_TERMS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MYPAGE_ACCOUNT_INFO) {
            AccountInfoRoute(
                onBack = navController::popBackStack,
                onNavigateToWithdraw = {
                    navController.navigate(Routes.MYPAGE_WITHDRAW) {
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MYPAGE_WITHDRAW) {
            WithdrawRoute(
                onBack = navController::popBackStack,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MYPAGE_TERMS) {
            TermsScreen(onBack = navController::popBackStack)
        }

        composable(Routes.MYPAGE_OPEN_LICENSE) {
            OpenLicenseScreen(
                onBack = navController::popBackStack,
                onNavigateToAccountInfo = {
                    navController.navigate(Routes.MYPAGE_ACCOUNT_INFO) {
                        launchSingleTop = true
                    }
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Routes.MYPAGE_PRIVACY_POLICY) {
                        launchSingleTop = true
                    }
                },
                onNavigateToTerms = {
                    navController.navigate(Routes.MYPAGE_TERMS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MYPAGE_PRIVACY_POLICY) {
            PrivacyPolicyScreen(onBack = navController::popBackStack)
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
                    navController.popBackStack()
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
                    navController.popBackStack()
                }
            )
        }
    }
}
