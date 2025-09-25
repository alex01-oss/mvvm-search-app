package com.loc.searchapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.loc.searchapp.navigation.graph.NavGraph
import com.loc.searchapp.navigation.graph.Route
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel


@Composable
fun MainContent(
    mainViewModel: MainViewModel,
) {
    val startDestination = mainViewModel.startDestination
    val authViewModel: AuthViewModel = hiltViewModel()
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
        )
    }
}