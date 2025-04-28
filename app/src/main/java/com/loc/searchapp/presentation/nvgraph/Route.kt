package com.loc.searchapp.presentation.nvgraph

sealed class Route(
    val route: String
) {
    data object OnBoardingScreen : Route(route = "onBoardingScreen")

    data object HomeScreen : Route(route = "homeScreen")
    data object SearchScreen : Route(route = "searchScreen")
    data object CartScreen : Route(route = "cartScreen")
    data object AccountScreen : Route(route = "accountScreen")

    data object ProductDetailsScreen : Route(route = "catalogDetailsScreen")

    data object AppStartNavigation : Route(route = "appStartNavigation")
    data object ProductsNavigation : Route(route = "productsNavigation")
    data object ProductsNavigatorScreen : Route(route = "productsNavigatorScreen")

    data object LoginScreen : Route(route = "loginScreen")
    data object RegisterScreen : Route(route = "registerScreen")

    data object LanguageScreen : Route(route = "languageScreen")
}