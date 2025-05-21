package com.loc.searchapp.feature.posts.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.feature.auth.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostInfoTopBar(
    post: Post,
    onEditClick: (Post) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    SharedTopBar(
        title = stringResource(id = R.string.post_info),
        actions = {
            if (authViewModel.isAdmin) {
                IconButton(onClick = { onEditClick(post) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        },
        showBackButton = true,
        onBackClick = onBackClick
    )
}