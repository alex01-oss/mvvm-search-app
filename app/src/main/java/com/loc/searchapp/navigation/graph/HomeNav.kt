package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loc.searchapp.feature.account.presentation.AccountScreen
import com.loc.searchapp.feature.cart.presentation.CartScreen
import com.loc.searchapp.feature.home.presentation.HomeScreen
import com.loc.searchapp.feature.product_details.presentation.DetailsScreen
import com.loc.searchapp.feature.product_details.viewmodel.DetailsViewModel
import com.loc.searchapp.feature.search.presentation.SearchScreen
import com.loc.searchapp.feature.search.viewmodel.SearchViewModel
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.feature.shared.viewmodel.HomeViewModel
import com.loc.searchapp.feature.shared.viewmodel.PostViewModel
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel
import com.loc.searchapp.navigation.utils.navigateToTab

fun NavGraphBuilder.homeScreens(
    navController: NavController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    homeViewModel: HomeViewModel,
    postViewModel: PostViewModel,
) {
    composable(route = Route.HomeScreen.route) {
        HomeScreen(
            authViewModel = authViewModel,
            postViewModel = postViewModel,
            viewModel = homeViewModel,
            onCategoryClick = { categoryId ->
                navigateToTab(navController, Route.SearchScreen.createRoute(categoryId))
            },
            onPostClick = { post ->
                navController.navigate(Route.PostDetailedScreen.createRoute(post.id))
            },
            onAvatarClick = {
                navigateToTab(navController, Route.AccountScreen.route)
            },
        )
    }

    composable(route = Route.SearchScreen.route) {
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
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: return@composable
        val detailsViewModel: DetailsViewModel = hiltViewModel()

        detailsViewModel.loadProduct(id)

        val state by detailsViewModel.detailsState

        DetailsScreen(
            state = state,
            onBackClick = { navController.navigateUp() },
            productViewModel = productViewModel,
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