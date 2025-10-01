package com.loc.searchapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    productViewModel: ProductViewModel
) {
    homeScreens(
        navController,
        authViewModel,
        postViewModel,
        productViewModel
    )
    authScreens(
        navController,
        authViewModel,
    )
    postScreens(
        navController,
        authViewModel,
        postViewModel,
    )
    settingsScreens(
        navController,
        authViewModel,
    )
}