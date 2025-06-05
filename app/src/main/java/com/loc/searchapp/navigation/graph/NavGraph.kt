package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.feature.onboarding.model.OnBoardingEvent
import com.loc.searchapp.feature.onboarding.presentation.OnBoardingScreen
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.navigation.presentation.ProductsNavigator

@Composable
fun NavGraph(
    startDestination: String,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                OnBoardingScreen(
                    event = { event ->
                        when (event) {
                            OnBoardingEvent.SaveAppEntry -> {
                                navController.navigate(Route.ProductsNavigation.route) {
                                    popUpTo(Route.AppStartNavigation.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }

        navigation(
            route = Route.ProductsNavigation.route,
            startDestination = Route.ProductsNavigatorScreen.route
        ) {
            composable(route = Route.ProductsNavigatorScreen.route) {
                ProductsNavigator(authViewModel = authViewModel)
            }
        }
    }
}