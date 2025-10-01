package com.loc.searchapp.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loc.searchapp.presentation.post_details.presentation.PostDetailedScreen
import com.loc.searchapp.presentation.post_editor.model.PostEditorState
import com.loc.searchapp.presentation.post_editor.presentation.PostEditorScreen
import com.loc.searchapp.presentation.posts.presentation.PostsScreen
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel

fun NavGraphBuilder.postScreens(
    navController: NavController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel
) {
    composable(route = Route.PostsScreen.route) {
        val postState by postViewModel.allPostsState.collectAsState()

        LaunchedEffect(Unit) {
            postViewModel.loadAllPosts()
        }

        PostsScreen(
            state = postState,
            viewModel = postViewModel,
            authViewModel = authViewModel,
            onPostClick = { post ->
                navController.navigate(Route.PostDetailedScreen.createRoute(post.id))
            },
            onBackClick = {
                navController.popBackStack()
            },
            onAddNewPost = {
                postViewModel.initCreateMode()
                navController.navigate(Route.PostEditorScreen.createRoute())
            }
        )
    }

    composable(
        route = Route.PostDetailedScreen.route,
        arguments = listOf(navArgument("postId") { type = NavType.IntType })
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getInt("postId") ?: -1
        val postState by postViewModel.postDetailsState.collectAsState()

        LaunchedEffect(postId) {
            if (postId != -1) {
                postViewModel.getPostById(postId)
            }
        }

        PostDetailedScreen(
            state = postState,
            authViewModel = authViewModel,
            onEditClick = { id ->
                postViewModel.initEditMode(id)
                navController.navigate(Route.PostEditorScreen.createRoute(id))
            },
            onBackClick = {
                navController.popBackStack()
            },
            postId = postId,
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
        val editorState by postViewModel.postEditorState.collectAsState()
        val actionState by postViewModel.postActionState.collectAsState()

        if (postId != -1 && editorState is PostEditorState.CreateMode) {
            postViewModel.initEditMode(postId)
        }

        PostEditorScreen(
            actionState = actionState,
            editorState = editorState,
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