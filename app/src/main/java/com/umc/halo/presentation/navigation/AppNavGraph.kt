package com.umc.halo.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umc.halo.presentation.calendar.CalendarScreen
import com.umc.halo.presentation.home.HomeRoute
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.mypage.AccountInfoRoute
import com.umc.halo.presentation.mypage.NotificationSettingsRoute
import com.umc.halo.presentation.mypage.SystemSettingsRoute
import com.umc.halo.presentation.mypage.WithdrawRoute
import com.umc.halo.presentation.mypage.screen.AccountManagementScreen
import com.umc.halo.presentation.mypage.screen.AnniversaryPlaceholderScreen
import com.umc.halo.presentation.mypage.screen.MyPageScreen
import com.umc.halo.presentation.mypage.screen.OpenLicenseScreen
import com.umc.halo.presentation.mypage.screen.PrivacyPolicyScreen
import com.umc.halo.presentation.mypage.screen.RelationshipInfoScreen
import com.umc.halo.presentation.mypage.screen.TermsScreen
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailRoute
import com.umc.halo.presentation.storybook.detail.StoryBookDetailScreen
import com.umc.halo.presentation.storybook.list.StorybookScreen
import com.umc.halo.presentation.themebox.ThemeBoxRoute
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

        // TODO: 온보딩 UI 확인용 임시 startDestination
        // 나중에 로그인 흐름 연결할 때 Routes.LOGIN 또는 Routes.SPLASH로 다시 변경 예정
        startDestination = Routes.HOME,

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
            LoginRoute()
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

        composable(Routes.STORYBOOK) {
            //스토리북
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
            AnniversaryPlaceholderScreen(onBack = navController::popBackStack)
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
                }
            )
        }

        composable(Routes.CHAPTER_PROGRESS) {
            Text(text = "Chapter Progress")
        }

        composable(Routes.CHAPTER_RESULT) {
            Text(text = "Chapter Result")
        }
    }
}
