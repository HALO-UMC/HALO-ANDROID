package com.umc.halo.presentation.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.umc.halo.presentation.theme.Primary500
import kotlinx.coroutines.delay


@Composable
fun SplashRoute(
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(4000)

        onNavigateToLogin()
    }

    SplashScreen()
}
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary500),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(600.dp)
        ) {
            DotLottieAnimation(
                source = DotLottieSource.Asset("splash.lottie"),
                autoplay = true,
                loop = false,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}