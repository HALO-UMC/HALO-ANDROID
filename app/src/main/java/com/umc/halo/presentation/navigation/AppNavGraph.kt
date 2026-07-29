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
    /**
     * 로그인 흐름(스플래시 → 로그인 → 약관 → 온보딩 → 홈)의 화면 이동.
     *
     * 이 흐름은 한 방향으로만 진행되고 뒤로 돌아가지 않으므로, 이동할 때마다 백스택을 비운다.
     * 그래야 홈에서 뒤로가기를 눌렀을 때 로그인 화면이 다시 뜨는 문제가 생기지 않는다.
     * (각 화면의 '뒤로가기'는 popBackStack 이 아니라 명시적 이동으로 처리한다.
     *  자동 로그인으로 중간 화면에 바로 진입하면 백스택에 이전 화면이 없기 때문)
     */
    fun navigateInAuthFlow(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            // 자동 로그인을 시도하고 로그인/약관/온보딩/홈 중 한 곳으로 보낸다
            SplashRoute(
                onNavigateToLogin = { navigateInAuthFlow(Routes.LOGIN) },
                onNavigateToTerms = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) },
                onNavigateToHome = { navigateInAuthFlow(Routes.HOME) }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(
                onNavigateBack = {
                    // 온보딩은 로그인 흐름의 마지막 단계라 돌아갈 이전 화면이 없는 경우가 많다.
                    // 그대로 popBackStack 하면 화면이 비어버리므로 이전 화면이 있을 때만 처리한다.
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onNavigateToHome = { navigateInAuthFlow(Routes.HOME) }
            )
        }

        composable(Routes.LOGIN) {
            // 로그인 성공 → 약관 미동의면 약관, 동의했으면 온보딩 또는 홈
            LoginRoute(
                onNavigateToTerms = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) },
                onNavigateToHome = { navigateInAuthFlow(Routes.HOME) }
            )
        }

        composable(Routes.TERMS) {
            TermsRoute(
                // 상단바 뒤로가기 → 로그아웃 처리 후 로그인 화면 (처리는 TermsViewModel 이 한다)
                onNavigateToLogin = { navigateInAuthFlow(Routes.LOGIN) },
                // '다음'(동의 내역 저장 성공) → 온보딩
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) }
            )
        }

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
            StorybookScreen(
                // 맞춤카드 및 시작전, 진행중 스토리북 카드 -> 스토리북 상세(목차)
                onNavigateToStorybookDetail = { storybookId ->
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        launchSingleTop = true
                    }
                },
                // 완료 카드 -> 테마함 (하단바 탭이라 하단바와 같은 백스택 옵션으로 전환)
                onNavigateToThemeBox = {
                    navController.navigate(Routes.THEME_BOX) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                }
            )
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
                    navController.navigate(Routes.storybookDetail(storybookId)) {
                        popUpTo(Routes.storybookDetail(storybookId)) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
