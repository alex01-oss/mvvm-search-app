package com.loc.searchapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.loc.searchapp.feature.auth.presentation.LoginScreen
import com.loc.searchapp.feature.auth.presentation.RegisterScreen
import com.loc.searchapp.feature.auth.viewmodel.AuthViewModel

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
                navController.navigate(Route.HomeScreen.route)
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
                navController.navigate(Route.HomeScreen.route)
            },
            viewModel = authViewModel
        )
    }
}