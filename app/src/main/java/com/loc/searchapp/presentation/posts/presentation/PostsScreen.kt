package com.loc.searchapp.presentation.posts.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.PostsSpacerSize
import com.loc.searchapp.presentation.posts.components.PostItemShimmer
import com.loc.searchapp.presentation.shared.components.PostItem
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.components.notifications.EmptyScreen
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel

@Composable
fun PostsScreen(
    viewModel: PostViewModel,
    authViewModel: AuthViewModel,
    onPostClick: (Post) -> Unit,
    onBackClick: () -> Unit,
    onAddNewPost: () -> Unit,
) {
    val postState = viewModel.postsState.collectAsState().value
    var showDeleteDialog by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.blog),
                onBackClick = onBackClick,
                showBackButton = true
            )
        },
        floatingActionButton = {
            if (authViewModel.isAdmin) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = onAddNewPost,
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_post),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    ) { paddingValues ->
        when (postState) {
            UiState.Loading -> PostItemShimmer()

            UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(id = R.string.empty_blog))
                }
            }

            is UiState.Error -> EmptyScreen(message = postState.message)

            is UiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(BasePadding),
                verticalArrangement = Arrangement.spacedBy(BasePadding)
            ) {
                items(postState.data) { post ->
                    PostItem(
                        post = post,
                        onClick = { onPostClick(post) },
                        onDeleteClick = { showDeleteDialog = post },
                        isAdmin = authViewModel.isAdmin
                    )
                }
                item { Spacer(modifier = Modifier.height(PostsSpacerSize)) }
            }
        }
    }

    if (showDeleteDialog != null) {
        AppDialog(
            title = stringResource(id = R.string.delete_post),
            message = stringResource(id = R.string.delete_post_dialog),
            confirmLabel = stringResource(id = R.string.delete),
            onConfirm = {
                showDeleteDialog?.let {
                    viewModel.deletePost(it.id)
                }
                showDeleteDialog = null
            },
            dismissLabel = stringResource(id = R.string.cancel),
            onDismiss = { showDeleteDialog = null }
        )
    }
}