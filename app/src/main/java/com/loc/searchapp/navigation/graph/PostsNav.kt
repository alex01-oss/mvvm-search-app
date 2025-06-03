package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loc.searchapp.feature.post_details.presentation.PostDetailedScreen
import com.loc.searchapp.feature.post_editor.presentation.PostEditorScreen
import com.loc.searchapp.feature.posts.presentation.PostsScreen
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.feature.shared.viewmodel.PostViewModel

fun NavGraphBuilder.postScreens(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel
) {
    composable(route = Route.PostsScreen.route) {

        PostsScreen(
            viewModel = postViewModel,
            authViewModel = authViewModel,
            onPostClick = { post ->
                navController.navigate(Route.PostDetailedScreen.createRoute(post.id))
            },
            onBackClick = {
                navController.popBackStack()
            },
            onAddNewPost = {
                navController.navigate(Route.PostEditorScreen.createRoute())
            }
        )
    }

    composable(
        route = Route.PostDetailedScreen.route,
        arguments = listOf(navArgument("postId") { type = NavType.IntType })
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getInt("postId") ?: -1

        LaunchedEffect(postId) {
            if (postId != -1) {
                postViewModel.getPostById(postId)
            }
        }

        val postState by postViewModel.postDetailsState.collectAsState()

        PostDetailedScreen(
            state = postState,
            authViewModel = authViewModel,
            onEditClick = { post ->
                navController.navigate(Route.PostEditorScreen.createRoute(post.id))
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    composable(
        route = Route.PostEditorScreen.route,
        arguments = listOf(navArgument("postId") {
            type = NavType.IntType
            defaultValue = -1
        })
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getInt("postId") ?: -1

        if (postId != -1) {
            LaunchedEffect(postId) {
                postViewModel.getPostById(postId)
            }
        }

        val postState by postViewModel.postDetailsState.collectAsState()
        val post = if (postId != -1) (postState as? UiState.Success)?.data else null

        val postActionState by postViewModel.postActionState.collectAsState()

        PostEditorScreen(
            post = post,
            postActionState = postActionState,
            viewModel = postViewModel,
            onBackClick = { navController.popBackStack() },
            onFinish = {
                navController.navigate(Route.PostsScreen.route) {
                    popUpTo(Route.PostsScreen.route) { inclusive = true }
                }
            }
        )
    }
}