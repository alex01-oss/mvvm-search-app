package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loc.searchapp.presentation.account.presentation.AccountScreen
import com.loc.searchapp.presentation.cart.presentation.CartScreen
import com.loc.searchapp.presentation.home.presentation.HomeScreen
import com.loc.searchapp.presentation.product_details.presentation.DetailsScreen
import com.loc.searchapp.presentation.product_details.viewmodel.DetailsViewModel
import com.loc.searchapp.presentation.search.presentation.SearchScreen
import com.loc.searchapp.presentation.search.viewmodel.SearchViewModel
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.home.viewmodel.HomeViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel
import com.loc.searchapp.navigation.utils.navigateToTab

fun NavGraphBuilder.homeScreens(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    productViewModel: ProductViewModel
) {
    composable(route = Route.HomeScreen.route) {
        val homeViewModel: HomeViewModel = hiltViewModel()

        HomeScreen(
            authViewModel = authViewModel,
            postViewModel = postViewModel,
            viewModel = homeViewModel,
            onCategoryClick = { categoryId ->
                navigateToTab(navController, Route.SearchScreen.createRoute(categoryId))
            },
            onPostClick = { id ->
                navController.navigate(Route.PostDetailedScreen.createRoute(id))
            },
            onAvatarClick = {
                navigateToTab(navController, Route.AccountScreen.route)
            },
            onAllPostsClick = {
                navigateToTab(navController, Route.PostsScreen.route)
            },
        )
    }

    composable(
        route = Route.SearchScreen.route + "?category_id={categoryId}",
        arguments = listOf(navArgument("categoryId") {
            type = NavType.IntType
            defaultValue = 1
        })
    ) { backStackEntry ->
        val searchViewModel: SearchViewModel = hiltViewModel()

        SearchScreen(
            viewModel = searchViewModel,
            productViewModel = productViewModel,
            navigateToDetails = { id ->
                navController.navigate(Route.ProductDetailsScreen.createRoute(id))
            },
        )
    }

    composable(route = Route.CartScreen.route) {
        LaunchedEffect(Unit) {
            productViewModel.setCartScreenActive(true)
            productViewModel.loadCart()
        }

        DisposableEffect(Unit) {
            onDispose {
                productViewModel.setCartScreenActive(false)
            }
        }

        CartScreen(
            viewModel = productViewModel,
            authViewModel = authViewModel,
            onAuthClick = {
                navController.navigate(Route.LoginScreen.route)
            },
            navigateToDetails = { id ->
                navController.navigate(Route.ProductDetailsScreen.createRoute(id))
            },
        )
    }

    composable(
        route = Route.ProductDetailsScreen.route,
        arguments = listOf(navArgument("productId") { type = NavType.IntType })
    ) { backStackEntry ->
        val detailsViewModel: DetailsViewModel = hiltViewModel()

        val productId = backStackEntry.arguments?.getInt("productId") ?: -1
        val state by detailsViewModel.detailsState.collectAsState()

        LaunchedEffect(productId) {
            if (productId != -1) {
                detailsViewModel.loadProduct(productId)
            }
        }

        DetailsScreen(
            state = state,
            onBackClick = { navController.navigateUp() },
            viewModel = productViewModel,
        )
    }

    composable(route = Route.AccountScreen.route) {
        AccountScreen(
            onAuthClick = { navController.navigate(Route.LoginScreen.route) },
            onSettingsClick = { navController.navigate(Route.SettingsScreen.route) },
            onLanguageClick = { navController.navigate(Route.LanguageScreen.route) },
            onAddPostClick = { navController.navigate(Route.PostsScreen.route) },
            onAboutClick = { navController.navigate(Route.AboutScreen.route) },
            onLogout = { navController.navigate(Route.LoginScreen.route) },
            viewModel = authViewModel,
        )
    }
}