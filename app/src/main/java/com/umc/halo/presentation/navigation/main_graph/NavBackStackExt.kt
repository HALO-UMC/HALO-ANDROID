package com.umc.halo.presentation.navigation.main_graph

import androidx.navigation.NavController

internal fun NavController.popBackStackIfCurrent(route: String) {
    if (currentDestination?.route == route) {
        popBackStack()
    }
}

internal fun NavController.navigateIfCurrent(
    route: String,
    block: NavController.() -> Unit
) {
    if (currentDestination?.route == route) {
        block()
    }
}
