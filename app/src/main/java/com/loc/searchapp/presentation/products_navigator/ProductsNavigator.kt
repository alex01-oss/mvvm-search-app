package com.loc.searchapp.presentation.products_navigator

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.account.AccountScreen
import com.loc.searchapp.presentation.auth.LoginScreen
import com.loc.searchapp.presentation.auth.RegisterScreen
import com.loc.searchapp.presentation.cart.CartScreen
import com.loc.searchapp.presentation.cart.CartViewModel
import com.loc.searchapp.presentation.details.DetailsEvent
import com.loc.searchapp.presentation.details.DetailsScreen
import com.loc.searchapp.presentation.details.DetailsScreenViewModel
import com.loc.searchapp.presentation.home.HomeScreen
import com.loc.searchapp.presentation.home.HomeViewModel
import com.loc.searchapp.presentation.nvgraph.Route
import com.loc.searchapp.presentation.products_navigator.components.BottomNavigationItem
import com.loc.searchapp.presentation.products_navigator.components.NewsBottomNavigation
import com.loc.searchapp.presentation.search.SearchScreen
import com.loc.searchapp.presentation.search.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductsNavigator() {
    val bottomNavigationItems = listOf(
        BottomNavigationItem(icon = R.drawable.home, text = "Home"),
        BottomNavigationItem(icon = R.drawable.search, text = "Search"),
        BottomNavigationItem(icon = R.drawable.shopping_cart, text = "Cart"),
        BottomNavigationItem(icon = R.drawable.person, text = "Account"),
    )

    val navController = rememberNavController()
    val backstackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = remember(key1 = backstackState) {
        when (backstackState?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.CartScreen.route -> 2
            Route.AccountScreen.route -> 3
            else -> 0
        }
    }

    val isBottomBarVisible = remember(key1 = backstackState) {
        backstackState?.destination?.route == Route.HomeScreen.route ||
                backstackState?.destination?.route == Route.SearchScreen.route ||
                backstackState?.destination?.route == Route.CartScreen.route ||
                backstackState?.destination?.route == Route.AccountScreen.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController,
                                route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController,
                                route = Route.SearchScreen.route
                            )

                            2 -> navigateToTab(
                                navController = navController,
                                route = Route.CartScreen.route
                            )

                            3 -> navigateToTab(
                                navController = navController,
                                route = Route.AccountScreen.route
                            )
                        }
                    },
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val products = viewModel.products.collectAsState()

                HomeScreen(
                    products = products,
                    navigateToSearch = {
                        navigateToTab(
                            navController = navController,
                            route = Route.SearchScreen.route
                        )
                    },
                    navigateToDetails = { product ->
                        navigateToDetails(
                            navController = navController,
                            product = product
                        )
                    },
                    onAdd = {},
                    onRemove = {}
                )
            }

            composable(route = Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value

                SearchScreen(
                    state = state,
                    event = { event ->
                        viewModel.viewModelScope.launch {
                            viewModel.onEvent(event)
                        }
                    },
                    navigateToDetails = {
                        navigateToDetails(
                            navController = navController,
                            product = it
                        )
                    }
                )
            }

            composable(route = Route.CartScreen.route) {
                val viewModel: CartViewModel = hiltViewModel()
                val cartItems = viewModel.cartItems.collectAsState()
                CartScreen(
                    navigateToDetails = { cartItem ->
                        navigateToDetails(
                            navController = navController,
                            product = cartItem as Product
                        )
                    },
                    cartItems = cartItems,
                    onDelete = {},
                )
            }

            composable(route = Route.AccountScreen.route) {
                AccountScreen(
                    onAuthClick = {
                        navController.navigate(Route.LoginScreen.route)
                    },
                    onLogoutClick = {
                        navController.navigate(Route.LoginScreen.route)
                    },
                    viewModel = hiltViewModel()
                )
            }

            composable(route = Route.DetailsScreen.route) {
                val viewModel: DetailsScreenViewModel = hiltViewModel()

                if (viewModel.sideEffect != null) {
                    Toast.makeText(LocalContext.current, viewModel.sideEffect, Toast.LENGTH_SHORT)
                        .show()
                }

                viewModel.onEvent(DetailsEvent.RemoveSideEffect)

                navController.previousBackStackEntry?.savedStateHandle?.get<Product?>("product")
                    ?.let { product ->
                        DetailsScreen(
                            product = product,
                            event = viewModel::onEvent,
                            navigateUp = { navController.navigateUp() },
                        )
                    }
            }

            composable(route = Route.LoginScreen.route) {
                LoginScreen(
                    navController = navController,
                    onRegisterClick = {
                        navController.navigate(Route.RegisterScreen.route)
                    }
                )
            }

            composable(route = Route.RegisterScreen.route) {
                RegisterScreen(
                    navController = navController,
                    onLoginClick = {
                        navController.navigate(Route.LoginScreen.route)
                    }
                )
            }
        }
    }
}

private fun navigateToTab(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}

private fun navigateToDetails(
    navController: NavController,
    product: Product

) {
    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
    navController.navigate(
        route = Route.DetailsScreen.route
    )
}