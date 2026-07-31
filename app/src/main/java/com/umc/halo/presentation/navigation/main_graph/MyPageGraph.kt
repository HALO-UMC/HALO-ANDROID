package com.umc.halo.presentation.navigation.main_graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
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
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val anniversaryViewModel: AnniversaryViewModel = viewModel(parentEntry)

            AnniversaryRoute(
                onBack = navController::popBackStack,
                viewModel = anniversaryViewModel
            )
        }

        composable(Routes.MYPAGE_SYSTEM_SETTINGS) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            SystemSettingsRoute(
                onBack = navController::popBackStack,
                viewModel = myPageViewModel
            )
        }

        composable(Routes.MYPAGE_NOTIFICATION_SETTINGS) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            NotificationSettingsRoute(
                onBack = navController::popBackStack,
                viewModel = myPageViewModel
            )
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
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            AccountInfoRoute(
                onBack = navController::popBackStack,
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

        composable(Routes.MYPAGE_WITHDRAW) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Graphs.MYPAGE)
            }
            val myPageViewModel: MyPageViewModel = hiltViewModel(parentEntry)

            WithdrawRoute(
                onBack = navController::popBackStack,
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
    }
}
