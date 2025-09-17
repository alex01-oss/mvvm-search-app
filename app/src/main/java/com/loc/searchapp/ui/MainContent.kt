package com.loc.searchapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.loc.searchapp.feature.onboarding.viewmodel.OnBoardingViewModel
import com.loc.searchapp.feature.shared.model.AuthState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.navigation.graph.NavGraph
import com.loc.searchapp.navigation.graph.Route


@Composable
fun MainContent(
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onBoardingViewModel: OnBoardingViewModel
) {
    val startDestination = mainViewModel.startDestination
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(startDestination, authState) {
        val shouldKeepSplash = startDestination == null ||
                (startDestination == Route.ProductsNavigation.route && authState == AuthState.Loading)

        if (!shouldKeepSplash && mainViewModel.splashCondition) {
            mainViewModel.hideSplash()
        }
    }

    val shouldShowContent = startDestination != null &&
            (startDestination == Route.AppStartNavigation.route || authState != AuthState.Loading)

    if (shouldShowContent) {
        NavGraph(
            startDestination = startDestination,
            authViewModel = authViewModel,
            onBoardingViewModel = onBoardingViewModel
        )
    }
}