package com.umc.halo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umc.halo.presentation.navigation.main_graph.calenderGraph
import com.umc.halo.presentation.navigation.main_graph.homeGraph
import com.umc.halo.presentation.navigation.main_graph.mypageGraph
import com.umc.halo.presentation.navigation.main_graph.storybookGraph
import com.umc.halo.presentation.navigation.main_graph.themeBoxGraph
import com.umc.halo.presentation.themebox.ThemeBoxRoute
import com.umc.halo.presentation.themebox.show_theme.ShowThemeRoute

// NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Graphs.AUTH,
        modifier = modifier
    ) {
        authGraph(navController)

        navigation(
            route = Graphs.MAIN,
            startDestination = Graphs.HOME
        ) {
            homeGraph(navController)
            calenderGraph(navController)
            storybookGraph(navController)
            themeBoxGraph(navController)
            mypageGraph(navController)
        }
    }
}