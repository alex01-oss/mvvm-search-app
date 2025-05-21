package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.feature.auth.viewmodel.AuthViewModel
import com.loc.searchapp.feature.posts.presentation.PostDetailedScreen
import com.loc.searchapp.feature.posts.presentation.PostEditorScreen
import com.loc.searchapp.feature.posts.presentation.PostsScreen
import com.loc.searchapp.feature.posts.viewmodel.PostViewModel

fun NavGraphBuilder.postScreens(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    composable(route = Route.PostsScreen.route) {
        val postViewModel: PostViewModel = hiltViewModel()
        val posts by postViewModel.posts.collectAsState()

        PostsScreen(
            posts = posts,
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
        val postViewModel: PostViewModel = hiltViewModel()

        val post by produceState<Post?>(initialValue = null, postId) {
            value = if (postId != -1) postViewModel.getPostById(postId) else null
        }

        post?.let { post ->
            PostDetailedScreen(
                post = post,
                authViewModel = authViewModel,
                onEditClick = {
                    navController.navigate(Route.PostEditorScreen.createRoute(post.id))
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
        val postViewModel: PostViewModel = hiltViewModel()

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