package com.loc.searchapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.loc.searchapp.presentation.login.presentation.LoginScreen
import com.loc.searchapp.presentation.register.presentation.RegisterScreen
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

fun NavGraphBuilder.authScreens(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    composable(route = Route.LoginScreen.route) {
        LoginScreen(
            onRegisterClick = {
                navController.navigate(Route.RegisterScreen.route)
            },
            onAuthenticated = {
                navController.popBackStack()
                navController.navigate(Route.HomeScreen.route) {
                    launchSingleTop = true
                }
            },
            viewModel = authViewModel
        )
    }

    composable(route = Route.RegisterScreen.route) {
        RegisterScreen(
            onLoginClick = {
                navController.navigate(Route.LoginScreen.route)
            },
            onAuthenticated = {
                navController.popBackStack()
                navController.navigate(Route.HomeScreen.route) {
                    launchSingleTop = true
                }
            },
            viewModel = authViewModel
        )
    }
}