package com.umc.halo.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.umc.halo.presentation.component.HaloScaffold
import com.umc.halo.presentation.navigation.AppNavGraph

@Composable
fun HaloApp() {
    val navController = rememberNavController()

    HaloScaffold(navController) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}