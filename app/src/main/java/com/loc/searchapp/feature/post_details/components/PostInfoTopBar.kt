package com.loc.searchapp.feature.post_details.components

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel


@Composable
fun PostInfoTopBar(
    post: Post,
    onEditClick: (Post) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current

    SharedTopBar(
        title = stringResource(id = R.string.post_info),
        actions = {
            if (authViewModel.isAdmin) {
                IconButton(onClick = { onEditClick(post) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            } else {
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:sale@pdtools.shop".toUri()
                    }
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Report , contentDescription = null)
                }
            }
        },
        showBackButton = true,
        onBackClick = onBackClick
    )
}