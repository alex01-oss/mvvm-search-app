package com.loc.searchapp.navigation.graph

sealed class Route(
    val route: String
) {
    data object OnBoardingScreen : Route(route = "onBoardingScreen")

    data object HomeScreen : Route(route = "homeScreen")

    data object SearchScreen : Route(route = "search_screen") {
        fun createRoute(categoryId: Int): String = "$route?category_id=$categoryId"
    }

    data object CartScreen : Route(route = "cartScreen")
    data object AccountScreen : Route(route = "accountScreen")

    data object ProductDetailsScreen : Route(route = "catalogDetailsScreen/{productId}") {
        fun createRoute(productId: Int) = "catalogDetailsScreen/$productId"
    }

    data object AppStartNavigation : Route(route = "appStartNavigation")
    data object ProductsNavigation : Route(route = "productsNavigation")

    data object ProductsNavigatorScreen : Route(route = "productsNavigatorScreen")

    data object LoginScreen : Route(route = "loginScreen")

    data object RegisterScreen : Route(route = "registerScreen")

    data object SettingsScreen : Route(route = "settingsScreen")

    data object LanguageScreen : Route(route = "languageScreen")

    data object AboutScreen : Route(route = "aboutScreen")

    data object PostsScreen : Route(route = "postsScreen")

    data object PostDetailedScreen : Route(route = "postDetailedScreen/{postId}") {
        fun createRoute(postId: Int) = "postDetailedScreen/$postId"
    }

    data object PostEditorScreen : Route(route = "postEditorScreen?postId={postId}") {
        fun createRoute(postId: Int? = null) = "postEditorScreen?postId=${postId ?: ""}"
    }
}