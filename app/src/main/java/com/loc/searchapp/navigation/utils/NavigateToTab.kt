package com.loc.searchapp.navigation.utils

import androidx.navigation.NavController

fun navigateToTab(
    navController: NavController, route: String
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}