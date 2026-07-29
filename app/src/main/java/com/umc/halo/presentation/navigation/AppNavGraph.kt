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
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {

    }
}
