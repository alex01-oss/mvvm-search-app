package com.loc.searchapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.loc.searchapp.presentation.about.presentation.AboutScreen
import com.loc.searchapp.presentation.language.presentation.LanguageScreen
import com.loc.searchapp.presentation.settings.presentation.SettingsScreen
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

fun NavGraphBuilder.settingsScreens(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    composable(route = Route.SettingsScreen.route) {
        SettingsScreen(
            viewModel = authViewModel,
            onBackClick = {
                navController.popBackStack()
            },
            onDeleteClick = {
                navController.navigate(Route.RegisterScreen.route)
            }
        )
    }

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