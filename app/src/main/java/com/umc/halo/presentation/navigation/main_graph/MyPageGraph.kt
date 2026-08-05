package com.umc.halo.presentation.navigation.main_graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.mypage.AccountInfoRoute
import com.umc.halo.presentation.mypage.AnniversaryRoute
import com.umc.halo.presentation.mypage.MyPageViewModel
import com.umc.halo.presentation.mypage.NotificationSettingsRoute
import com.umc.halo.presentation.mypage.SystemSettingsRoute
import com.umc.halo.presentation.mypage.WithdrawRoute
import com.umc.halo.presentation.mypage.anniversary.AnniversaryViewModel
import com.umc.halo.presentation.mypage.screen.AccountManagementScreen
import com.umc.halo.presentation.mypage.screen.MyPageScreen
import com.umc.halo.presentation.mypage.screen.OpenLicenseScreen
import com.umc.halo.presentation.mypage.screen.PrivacyPolicyScreen
import com.umc.halo.presentation.mypage.screen.RelationshipInfoScreen
import com.umc.halo.presentation.mypage.screen.TermsScreen
import com.umc.halo.presentation.navigation.Graphs
import com.umc.halo.presentation.navigation.Routes

fun NavGraphBuilder.mypageGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.MYPAGE,
        startDestination = Routes.MYPAGE
    ) {
        composable(Routes.MYPAGE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)
            val uiState by myPageViewModel.uiState.collectAsState()

            MyPageScreen(
                uiState = uiState,
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
            RelationshipInfoScreen(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_RELATIONSHIP_INFO) }
            )
        }

        composable(Routes.MYPAGE_ANNIVERSARY) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val anniversaryViewModel: AnniversaryViewModel = viewModel(parentEntry)

            AnniversaryRoute(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_ANNIVERSARY) },
                viewModel = anniversaryViewModel
            )
        }

        composable(Routes.MYPAGE_SYSTEM_SETTINGS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            SystemSettingsRoute(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_SYSTEM_SETTINGS) },
                viewModel = myPageViewModel
            )
        }

        composable(Routes.MYPAGE_NOTIFICATION_SETTINGS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            NotificationSettingsRoute(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_NOTIFICATION_SETTINGS) },
                viewModel = myPageViewModel
            )
        }

        composable(Routes.MYPAGE_ACCOUNT_MANAGEMENT) {
            AccountManagementScreen(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_ACCOUNT_MANAGEMENT) },
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

        composable(Routes.MYPAGE_ACCOUNT_INFO) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            AccountInfoRoute(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_ACCOUNT_INFO) },
                viewModel = myPageViewModel,
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

        composable(Routes.MYPAGE_WITHDRAW) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            WithdrawRoute(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_WITHDRAW) },
                viewModel = myPageViewModel,
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
            TermsScreen(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_TERMS) }
            )
        }

        composable(Routes.MYPAGE_OPEN_LICENSE) {
            OpenLicenseScreen(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_OPEN_LICENSE) },
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
            PrivacyPolicyScreen(
                onBack = { navController.popBackStackIfCurrent(Routes.MYPAGE_PRIVACY_POLICY) }
            )
        }
    }
}
