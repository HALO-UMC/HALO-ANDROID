package com.umc.halo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.umc.halo.core.audio.BgmPlaybackManager
import com.umc.halo.presentation.HaloApp
import com.umc.halo.presentation.theme.HaloTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// @AndroidEntryPoint: 이 액티비티가 호스팅하는 Compose 화면에서 hiltViewModel() 로 VM 주입이 가능함
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Activity 가 하나뿐인 앱이라 ON_STOP/ON_START 를 곧 "백그라운드 진입/복귀"로 취급함
    @Inject
    lateinit var bgmPlaybackManager: BgmPlaybackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                bgmPlaybackManager.pauseForBackground()
            }

            override fun onStart(owner: LifecycleOwner) {
                bgmPlaybackManager.resumeFromBackground()
            }
        })

        setContent {
            HaloTheme {
                HaloApp()
            }
        }
    }
}