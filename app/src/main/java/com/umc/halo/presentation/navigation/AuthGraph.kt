package com.umc.halo.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.login.LoginRoute
import com.umc.halo.presentation.login.ReloginRoute
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
         * 로그인 흐름(스플래시 → 로그인 → 약관 → 온보딩 → 홈)의 화면 이동
         *
         * 이 흐름은 한 방향으로만 진행되고 뒤로 돌아가지 않으므로 이동할 때마다 백스택을 비움
         * 그래야 홈에서 뒤로가기를 눌렀을 때 로그인 화면이 다시 뜨는 문제가 생기지 않음
         * (각 화면의 '뒤로가기'는 popBackStack 이 아니라 명시적 이동으로 처리
         *  자동 로그인으로 중간 화면에 바로 진입하면 백스택에 이전 화면이 없기 때문)
         */
        fun navigateInAuthFlow(route: String) {
            navController.navigate(route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }

        composable(Routes.SPLASH) {
            // 자동 로그인을 시도하고 로그인/약관/온보딩/홈 중 한 곳으로 보냄
            SplashRoute(
                onNavigateToLogin = { navigateInAuthFlow(Routes.LOGIN) },
                onNavigateToTerms = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) },
                onNavigateToHome = { navigateInAuthFlow(Graphs.MAIN) }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(
                // 온보딩 첫 단계의 < → 약관동의로 되돌아감
                // 되돌아가서 다시 고른 동의 내역은 '다음'을 누를 때 서버에 덮어씀
                onNavigateBack = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToHome = { navigateInAuthFlow(Graphs.MAIN) }
            )
        }

        composable(Routes.LOGIN) {
            // 로그인 성공 → 약관 미동의면 약관, 동의했으면 온보딩 또는 홈
            LoginRoute(
                onNavigateToTerms = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) },
                onNavigateToHome = { navigateInAuthFlow(Graphs.MAIN) }
            )
        }

        composable(Routes.RELOGIN) {
            // 마이페이지에서 로그아웃하면 재로그인 화면으로 이동
            // 로그인 이후 분기는 로그인 화면과 동일
            ReloginRoute(
                onNavigateToTerms = { navigateInAuthFlow(Routes.TERMS) },
                onNavigateToOnboarding = { navigateInAuthFlow(Routes.ONBOARDING) },
                onNavigateToHome = { navigateInAuthFlow(Graphs.MAIN) }
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