package com.umc.halo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.umc.halo.presentation.HaloApp
import com.umc.halo.presentation.theme.HaloTheme
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint: 이 액티비티가 호스팅하는 Compose 화면에서 hiltViewModel() 로 VM 주입이 가능함
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HaloTheme {
                HaloApp()
            }
        }
    }
}