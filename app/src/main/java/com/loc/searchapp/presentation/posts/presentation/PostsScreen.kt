package com.loc.searchapp.presentation.posts.presentation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.presentation.posts.components.PostItemShimmer
import com.loc.searchapp.presentation.shared.components.PostItem
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.components.notifications.EmptyContent
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel

@Composable
fun PostsScreen(
    state: UiState<List<Post>>,
    viewModel: PostViewModel,
    authViewModel: AuthViewModel,
    onPostClick: (Post) -> Unit,
    onBackClick: () -> Unit,
    onAddNewPost: () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf<Post?>(null) }

    val context = LocalContext.current

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
                    modifier = Modifier.semantics {
                        contentDescription = context.getString(R.string.add_post)
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = themeAdaptiveColor()
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(BasePadding),
            verticalArrangement = Arrangement.spacedBy(BasePadding)
        ) {
            when (state) {
                UiState.Loading -> items(3) { PostItemShimmer() }

                is UiState.Error -> {
                    item {
                        EmptyContent(
                            message = state.message,
                            iconId = R.drawable.ic_network_error,
                        )
                    }
                }

                UiState.Empty -> {
                    item {
                        EmptyContent(
                            message = stringResource(id = R.string.error),
                            iconId = R.drawable.ic_network_error,
                        )
                    }
                }

                is UiState.Success ->
                    items(state.data) { post ->
                        PostItem(
                            post = post,
                            onClick = { onPostClick(post) },
                            onDeleteClick = { showDeleteDialog = post },
                            isAdmin = authViewModel.isAdmin
                        )
                    }
            }
            item { Spacer(modifier = Modifier.height(TopLogoHeight)) }
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