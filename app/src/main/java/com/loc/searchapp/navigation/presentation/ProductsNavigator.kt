package com.loc.searchapp.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.common.BottomNavItem
import com.loc.searchapp.feature.auth.viewmodel.AuthViewModel
import com.loc.searchapp.core.ui.components.loading.LoadingScreen
import com.loc.searchapp.feature.auth.model.AuthState
import com.loc.searchapp.navigation.components.BottomNavigation
import com.loc.searchapp.navigation.graph.Route
import com.loc.searchapp.navigation.graph.authScreens
import com.loc.searchapp.navigation.graph.homeScreens
import com.loc.searchapp.navigation.utils.navigateToTab
import com.loc.searchapp.navigation.graph.postScreens
import com.loc.searchapp.navigation.graph.settingsScreens

@Composable
fun ProductsNavigator(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    if (authState == AuthState.Loading) {
        LoadingScreen()
    } else {
        val startDestination = when (authState) {
            is AuthState.Authenticated -> Route.HomeScreen.route
            else -> Route.LoginScreen.route
        }

        val backstackState = navController.currentBackStackEntryAsState().value

        val bottomNavigationItems = listOf(
            BottomNavItem(
                icon = R.drawable.home, text = stringResource(id = R.string.home)
            ),
            BottomNavItem(
                icon = R.drawable.search, text = stringResource(id = R.string.search)
            ),
            BottomNavItem(
                icon = R.drawable.cart, text = stringResource(id = R.string.cart)
            ),
            BottomNavItem(
                icon = R.drawable.person, text = stringResource(id = R.string.account)
            ),
        )

        val selectedItem by remember(backstackState) {
            derivedStateOf {
                when (backstackState?.destination?.route) {
                    Route.HomeScreen.route -> 0
                    Route.SearchScreen.route -> 1
                    Route.CartScreen.route -> 2
                    Route.AccountScreen.route -> 3
                    else -> 0
                }
            }
        }

        val isBottomBarVisible = remember(key1 = backstackState) {
            listOf(
                Route.HomeScreen.route,
                Route.SearchScreen.route,
                Route.CartScreen.route,
                Route.AccountScreen.route
            ).contains(backstackState?.destination?.route)
        }

        Scaffold(
            modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                if (isBottomBarVisible) {
                    BottomNavigation(
                        items = bottomNavigationItems,
                        selected = selectedItem,
                        onItemClick = { index ->
                            when (index) {
                                0 -> navigateToTab(navController, Route.HomeScreen.route)
                                1 -> navigateToTab(navController, Route.SearchScreen.route)
                                2 -> navigateToTab(navController, Route.CartScreen.route)
                                3 -> navigateToTab(navController, Route.AccountScreen.route)
                            }
                        },
                    )
                }
            }
        ) {
            val bottomPadding = it.calculateBottomPadding()

            NavHost(
                modifier = Modifier.padding(bottom = bottomPadding),
                navController = navController,
                startDestination = startDestination
            ) {
                mainNavGraph(navController, authViewModel)
            }
        }
    }
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    homeScreens(navController, authViewModel)
    authScreens(navController, authViewModel)
    postScreens(navController, authViewModel)
    settingsScreens(navController)
}