package com.loc.searchapp.navigation.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.common.BottomNavItem
import com.loc.searchapp.core.ui.components.loading.LoadingScreen
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.feature.shared.model.AuthState
import com.loc.searchapp.feature.shared.network.NetworkStatus
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.feature.shared.viewmodel.ConnectivityViewModel
import com.loc.searchapp.feature.shared.viewmodel.HomeViewModel
import com.loc.searchapp.feature.shared.viewmodel.PostViewModel
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel
import com.loc.searchapp.navigation.components.BottomNavigation
import com.loc.searchapp.navigation.graph.Route
import com.loc.searchapp.navigation.graph.authScreens
import com.loc.searchapp.navigation.graph.homeScreens
import com.loc.searchapp.navigation.graph.postScreens
import com.loc.searchapp.navigation.graph.settingsScreens
import com.loc.searchapp.navigation.utils.navigateToTab

@Composable
fun ProductsNavigator(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val postViewModel: PostViewModel = hiltViewModel()

    val authState by authViewModel.authState.collectAsState()

    val startDestination = when (authState) {
        is AuthState.Authenticated -> Route.HomeScreen.route
        is AuthState.Unauthenticated, is AuthState.Error -> Route.LoginScreen.route
        AuthState.Loading -> {
            LoadingScreen()
            return
        }
    }

    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val networkStatus by connectivityViewModel.networkStatus.collectAsState()
    val backstackState by navController.currentBackStackEntryAsState()
    val context = LocalContext.current


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
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                startDestination = startDestination
            ) {
                mainNavGraph(
                    navController,
                    authViewModel,
                    productViewModel,
                    homeViewModel,
                    postViewModel
                )
            }

            if (networkStatus != NetworkStatus.Available) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            start = MediumPadding1,
                            end = MediumPadding1,
                            bottom = if (isBottomBarVisible) paddingValues.calculateBottomPadding()
                            else TopLogoHeight
                        ),
                    action = {
                        TextButton(onClick = {
                            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                            context.startActivity(intent)
                        }) {
                            Text(stringResource(id = R.string.settings))
                        }
                    }
                ) {
                    Text(stringResource(R.string.no_internet_connection, networkStatus.name))
                }
            }
        }
    }
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    homeViewModel: HomeViewModel,
    postViewModel: PostViewModel
) {
    homeScreens(
        navController,
        authViewModel,
        productViewModel,
        homeViewModel,
        postViewModel
    )
    authScreens(
        navController,
        authViewModel
    )
    postScreens(
        navController,
        authViewModel,
        postViewModel
    )
    settingsScreens(
        navController,
        authViewModel
    )
}