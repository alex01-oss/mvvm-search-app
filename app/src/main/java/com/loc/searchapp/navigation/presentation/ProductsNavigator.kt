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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.common.BottomNavItem
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.navigation.components.BottomNavigation
import com.loc.searchapp.navigation.graph.Route
import com.loc.searchapp.navigation.graph.mainNavGraph
import com.loc.searchapp.navigation.utils.navigateToTab
import com.loc.searchapp.presentation.shared.components.loading.LoadingScreen
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.network.NetworkStatus
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.ConnectivityViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel

@Composable
fun ProductsNavigator(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
) {
    val navController = rememberNavController()

    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val postViewModel: PostViewModel = hiltViewModel()
    val productViewModel: ProductViewModel = hiltViewModel()

    val networkStatus by connectivityViewModel.networkStatus.collectAsState()
    val authState by authViewModel.authState.collectAsState()
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

    val isBottomBarVisible = remember(key1 = backstackState) {
        val currentRoute = backstackState?.destination?.route
        currentRoute != null && (
                currentRoute == Route.HomeScreen.route ||
                        currentRoute.startsWith(Route.SearchScreen.route) ||
                        currentRoute == Route.CartScreen.route ||
                        currentRoute == Route.AccountScreen.route
                )
    }

    val selectedItem by remember(backstackState) {
        derivedStateOf {
            when {
                backstackState?.destination?.route == Route.HomeScreen.route -> 0
                backstackState?.destination?.route?.startsWith(Route.SearchScreen.route) == true -> 1
                backstackState?.destination?.route == Route.CartScreen.route -> 2
                backstackState?.destination?.route == Route.AccountScreen.route -> 3
                else -> 0
            }
        }
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
                            0 -> {
                                while (navController.currentDestination?.route?.startsWith(
                                        Route.SearchScreen.route
                                    ) == true
                                ) {
                                    navController.popBackStack()
                                }
                                navController.navigate(Route.HomeScreen.route) {
                                    popUpTo(Route.HomeScreen.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }

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
            when (authState) {
                is AuthState.Loading -> {
                    LoadingScreen()
                }

                else -> {
                    NavHost(
                        navController = navController,
                        startDestination = when (authState) {
                            is AuthState.Authenticated -> Route.HomeScreen.route
                            is AuthState.Unauthenticated, is AuthState.Error -> Route.LoginScreen.route
                            else -> Route.LoginScreen.route
                        }
                    ) {
                        mainNavGraph(navController, authViewModel, postViewModel, productViewModel)
                    }
                }
            }

            if (networkStatus != NetworkStatus.Available) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            start = BasePadding,
                            end = BasePadding,
                            bottom = if (isBottomBarVisible) paddingValues.calculateBottomPadding()
                            else TopLogoHeight
                        )
                        .semantics(mergeDescendants = true) {
                            liveRegion = LiveRegionMode.Polite
                        },
                    action = {
                        TextButton(onClick = {
                            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                            context.startActivity(intent)
                        }) {
                            Text(stringResource(id = R.string.settings))
                        }
                    }
                ) {
                    Text(
                        stringResource(
                            R.string.no_internet_connection,
                            networkStatus.name
                        )
                    )
                }
            }
        }
    }
}