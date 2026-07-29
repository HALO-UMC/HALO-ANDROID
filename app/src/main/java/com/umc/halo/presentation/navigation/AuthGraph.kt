package com.umc.halo.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.onboarding.OnboardingRoute
import com.umc.halo.presentation.splash.SplashRoute
import com.umc.halo.presentation.terms.TermsRoute

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    navigation(
        route = Graphs.AUTH,
        startDestination = Routes.SPLASH
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
    }
}