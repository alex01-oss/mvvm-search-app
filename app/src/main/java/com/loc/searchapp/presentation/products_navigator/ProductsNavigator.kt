package com.loc.searchapp.presentation.products_navigator

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.BottomNavItem
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.account.AccountScreen
import com.loc.searchapp.presentation.auth.LoginScreen
import com.loc.searchapp.presentation.auth.RegisterScreen
import com.loc.searchapp.presentation.cart.CartScreen
import com.loc.searchapp.presentation.catalog.CatalogScreen
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.home.HomeScreen
import com.loc.searchapp.presentation.home.HomeViewModel
import com.loc.searchapp.presentation.language.LanguageScreen
import com.loc.searchapp.presentation.nvgraph.Route
import com.loc.searchapp.presentation.posts.PostDetailedScreen
import com.loc.searchapp.presentation.posts.PostEditorScreen
import com.loc.searchapp.presentation.posts.PostViewModel
import com.loc.searchapp.presentation.posts.PostsScreen
import com.loc.searchapp.presentation.product_details.DetailsEvent
import com.loc.searchapp.presentation.product_details.DetailsScreen
import com.loc.searchapp.presentation.product_details.DetailsViewModel
import com.loc.searchapp.presentation.products_navigator.components.NewsBottomNavigation
import com.loc.searchapp.presentation.search.SearchScreen
import com.loc.searchapp.presentation.search.SearchViewModel

@Composable
fun ProductsNavigator(
    modifier: Modifier = Modifier,
) {
    val bottomNavigationItems = listOf(
        BottomNavItem(
            icon = R.drawable.home, text = stringResource(id = R.string.home)
        ),
        BottomNavItem(
            icon = R.drawable.search, text = stringResource(id = R.string.search)
        ),
        BottomNavItem(
            icon = R.drawable.shopping_cart, text = stringResource(id = R.string.cart)
        ),
        BottomNavItem(
            icon = R.drawable.person, text = stringResource(id = R.string.account)
        ),
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
        backstackState?.destination?.route == Route.HomeScreen.route || backstackState?.destination?.route == Route.SearchScreen.route || backstackState?.destination?.route == Route.CartScreen.route || backstackState?.destination?.route == Route.AccountScreen.route
    }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val detailsViewModel: DetailsViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val productViewModel: ProductViewModel = hiltViewModel()
    val postViewModel: PostViewModel = hiltViewModel()

    Scaffold(
        modifier.fillMaxSize(), bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController, route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController, route = Route.SearchScreen.route
                            )

                            2 -> navigateToTab(
                                navController = navController, route = Route.CartScreen.route
                            )

                            3 -> navigateToTab(
                                navController = navController, route = Route.AccountScreen.route
                            )
                        }
                    },
                )
            }
        }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.HomeScreen.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    postViewModel = postViewModel,
                    viewModel = homeViewModel,
                    onCategoryClick = {
                        navController.navigate(
                            Route.CatalogScreen.route
                        )
                    },
                    onPostClick = { post ->
                        navController.navigate(
                            Route.PostDetailedScreen.createRoute(post.id)
                        )
                    },
                )
            }

            composable(route = Route.CatalogScreen.route) {
                val products = homeViewModel.catalogFlow.collectAsLazyPagingItems()

                CatalogScreen(
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
                    products = products,
                    authViewModel = authViewModel,
                    productViewModel = productViewModel,
                    onAuthClick = {
                        navController.navigate(
                            Route.LoginScreen.route
                        )
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }

            composable(route = Route.SearchScreen.route) {
                val state = searchViewModel.state.value

                SearchScreen(
                    state = state, event = searchViewModel::onEvent,
                    navigateToDetails = {
                        navigateToDetails(
                            navController = navController, product = it
                        )
                    },
                    viewModel = searchViewModel,
                    productViewModel = productViewModel,
                    homeViewModel = homeViewModel,
                )
            }

            composable(route = Route.CartScreen.route) {
                val cartItems = productViewModel.cartItems.collectAsState()

                CartScreen(
                    navigateToDetails = { cartItem ->
                        navigateToDetails(
                            navController = navController, product = cartItem.product
                        )
                    },
                    cartItems = cartItems,
                    viewModel = productViewModel,
                    cartModified = productViewModel.cartModified,
                    onCartUpdated = { productViewModel.cartModified = false },
                    authViewModel = authViewModel,
                    onAuthClick = {
                        navController.navigate(Route.LoginScreen.route)
                    },
                )
            }

            composable(route = Route.AccountScreen.route) {
                AccountScreen(
                    onAuthClick = {
                        navController.navigate(Route.LoginScreen.route)
                    },
                    onLanguageClick = {
                        navController.navigate(Route.LanguageScreen.route)
                    },
                    onAddPostClick = {
                        navController.navigate(Route.PostsScreen.route)
                    },
                    viewModel = authViewModel,
                    productViewModel = productViewModel,
                )
            }

            composable(route = Route.ProductDetailsScreen.route) {
                if (detailsViewModel.sideEffect != null) {
                    Toast.makeText(
                        LocalContext.current, detailsViewModel.sideEffect, Toast.LENGTH_SHORT
                    ).show()
                }
                detailsViewModel.onEvent(DetailsEvent.RemoveSideEffect)

                navController.previousBackStackEntry?.savedStateHandle?.get<Product?>("product")
                    ?.let { product ->
                        DetailsScreen(
                            event = detailsViewModel::onEvent,
                            onBackClick = { navController.navigateUp() },
                            product = product,
                        )
                    }
            }

            composable(route = Route.LoginScreen.route) {
                LoginScreen(
                    navController = navController, onRegisterClick = {
                        navController.navigate(Route.RegisterScreen.route)
                    }, viewModel = authViewModel
                )
            }

            composable(route = Route.RegisterScreen.route) {
                RegisterScreen(
                    navController = navController, onLoginClick = {
                        navController.navigate(Route.LoginScreen.route)
                    }, viewModel = authViewModel
                )
            }

            composable(route = Route.LanguageScreen.route) {
                LanguageScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                )
            }

            composable(route = Route.PostsScreen.route) {
                val posts by postViewModel.posts.collectAsState()

                PostsScreen(
                    posts = posts,
                    viewModel = postViewModel,
                    onPostClick = { post ->
                        navController.navigate(
                            Route.PostDetailedScreen.createRoute(post.id)
                        )
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAddNewPost = {
                        navController.navigate(
                            Route.PostEditorScreen.createRoute()
                        )
                    }
                )
            }

            composable(
                route = Route.PostDetailedScreen.route,
                arguments = listOf(
                    navArgument("postId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId") ?: -1

                val post by produceState<Post?>(initialValue = null, postId) {
                    value = if (postId != -1) postViewModel.getPostById(postId) else null
                }

                post?.let { post ->
                    PostDetailedScreen(
                        post = post,
                        onEditClick = {
                            navController.navigate(
                                Route.PostEditorScreen.createRoute(post.id)
                            )
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = Route.PostEditorScreen.route,
                arguments = listOf(navArgument("postId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId")?.takeIf { it != -1 }

                val post by produceState<Post?>(initialValue = null, postId) {
                    value = postId?.let { postViewModel.getPostById(it) }
                }

                PostEditorScreen(
                    post = post,
                    viewModel = postViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFinish = {
                        navController.navigate(Route.PostsScreen.route)
                    }

                )
            }
        }
    }
}

private fun navigateToTab(
    navController: NavController, route: String
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
    navController: NavController, product: Product

) {
    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
    navController.navigate(
        route = Route.ProductDetailsScreen.route
    )
}