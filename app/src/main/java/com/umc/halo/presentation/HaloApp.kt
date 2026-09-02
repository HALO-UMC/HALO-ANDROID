package com.umc.halo.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.umc.halo.core.audio.BgmPlaybackManager
import com.umc.halo.core.logging.AnalyticsLogger
import com.umc.halo.presentation.component.HaloScaffold
import com.umc.halo.presentation.navigation.AppNavGraph
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject

@Composable
fun HaloApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            HaloAppEntryPoint::class.java
        )
    }
    val bgmPlaybackManager = entryPoint.bgmPlaybackManager()

    HaloScaffold(
        navController = navController,
        onAppExit = bgmPlaybackManager::stopForAppExit
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            analyticsLogger = entryPoint.analyticsLogger(),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HaloAppEntryPoint {
    fun bgmPlaybackManager(): BgmPlaybackManager
    fun analyticsLogger(): AnalyticsLogger
}
