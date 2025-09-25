package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.navigation.presentation.ProductsNavigator
import com.loc.searchapp.presentation.onboarding.model.OnBoardingEvent
import com.loc.searchapp.presentation.onboarding.presentation.OnBoardingScreen
import com.loc.searchapp.presentation.onboarding.viewmodel.OnBoardingViewModel
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    startDestination: String,
    authViewModel: AuthViewModel,
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
                val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()

                OnBoardingScreen(
                    event = { event ->
                        onBoardingViewModel.onEvent(event)

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