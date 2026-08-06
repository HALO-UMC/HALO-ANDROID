package com.umc.halo.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.umc.halo.core.audio.BgmPlaybackManager
import com.umc.halo.presentation.component.HaloScaffold
import com.umc.halo.presentation.navigation.AppNavGraph
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
fun HaloApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val bgmPlaybackManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            BgmPlaybackEntryPoint::class.java
        ).bgmPlaybackManager()
    }

    HaloScaffold(
        navController = navController,
        onAppExit = bgmPlaybackManager::stopForAppExit
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BgmPlaybackEntryPoint {
    fun bgmPlaybackManager(): BgmPlaybackManager
}
