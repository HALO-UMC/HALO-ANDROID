package com.umc.halo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.umc.halo.presentation.navigation.AppNavGraph
import com.umc.halo.presentation.theme.HaloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HaloTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}