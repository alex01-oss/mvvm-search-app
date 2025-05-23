package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.feature.onboarding.presentation.OnBoardingScreen
import com.loc.searchapp.feature.onboarding.viewmodel.OnBoardingViewModel
import com.loc.searchapp.navigation.presentation.ProductsNavigator

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    event = viewModel::onEvent
                )
            }
        }

        navigation(
            route = Route.ProductsNavigation.route,
            startDestination = Route.ProductsNavigatorScreen.route
        ) {
            composable(route = Route.ProductsNavigatorScreen.route) {
                ProductsNavigator()
            }
        }
    }
}