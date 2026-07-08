package com.umc.halo.presentation.navigation

import android.R
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState


//NavHost + BottomBar 표시 여부 + 화면 route 연결
@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = BottomNavItem.entries.map { it.route }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { padding ->

        //--Route와 일치하는 화면 출력
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.SPLASH) {
                Text(text = "Splash")
            }

            composable(Routes.ONBOARDING) {
                Text(text = "Onboarding")
            }

            composable(Routes.LOGIN) {
                Text(text = "Login")
            }

            composable(Routes.HOME) {
                //홈화면
                Text(text = "Home")
            }

            composable(Routes.CALENDAR) {
                //캘린더
                Text(text = "Calendar")
            }

            composable(Routes.THEME_BOX) {
                //테마함
                Text(text = "Theme Box")
            }

            composable(Routes.STORYBOOK) {
                //스토리북
                Text(text = "Storybook")
            }

            composable(Routes.MYPAGE) {
                //마이페이지
                Text(text = "MyPage")
            }

            composable(Routes.STORYBOOK_DETAIL) {
                Text(text = "Storybook Detail")
            }

            composable(Routes.CHAPTER_PROGRESS) {
                Text(text = "Chapter Progress")
            }

            composable(Routes.CHAPTER_RESULT) {
                Text(text = "Chapter Result")
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface //BottomNav 배경 색
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        //---백스택 관리
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (currentRoute == item.route) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            }
                        ),
                        tint = Color.Unspecified,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (currentRoute == item.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        //fontFamily = 폰트 추가 필요
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    //---colors 이용 여부 논의 필요
                    //selectedTextColor = MaterialTheme.colorScheme.primary, //선택 시 라벨 색
                    //selectedIconColor = MaterialTheme.colorScheme.primary, //선택 시 아이콘 색
                    indicatorColor = Color.Transparent //인디케이터 투명화
                )
            )
        }
    }
}