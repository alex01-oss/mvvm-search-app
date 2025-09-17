package com.loc.searchapp.feature.post_editor.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.feature.shared.components.SharedTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    isNewPost: Boolean,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    SharedTopBar(
        title =
            if (isNewPost) stringResource(id = R.string.create_post)
            else stringResource(id = R.string.edit_post),
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(Icons.Default.Save, contentDescription = stringResource(id = R.string.save_changes))
            }
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = stringResource(id = R.string.close))
            }
        },
        showBackButton = true,
        onBackClick = onBackClick
    )
}