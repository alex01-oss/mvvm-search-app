package com.loc.searchapp.presentation.posts.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.presentation.common.components.SharedTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostInfoTopBar(
    post: Post,
    onEditClick: (Post) -> Unit,
    onBackClick: () -> Unit
) {
    SharedTopBar(
        title = stringResource(id = R.string.post_info),
        actions = {
            IconButton(onClick = { onEditClick(post) }) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
        },
        showBackButton = true,
        onBackClick = onBackClick
    )
}