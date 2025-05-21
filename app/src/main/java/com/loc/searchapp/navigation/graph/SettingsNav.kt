package com.loc.searchapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.loc.searchapp.feature.about.presentation.AboutScreen
import com.loc.searchapp.feature.language.presentation.LanguageScreen

fun NavGraphBuilder.settingsScreens(navController: NavController) {
    composable(route = Route.AboutScreen.route) {
        AboutScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    composable(route = Route.LanguageScreen.route) {
        LanguageScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}