package com.loc.searchapp.presentation.post_details.components

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
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel


@Composable
fun PostInfoTopBar(
    postId: Int,
    onEditClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current

    SharedTopBar(
        title = stringResource(R.string.post_info),
        actions = {
            if (authViewModel.isAdmin) {
                IconButton(onClick = { onEditClick(postId) }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_post)
                    )
                }
            } else {
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:sale@pdtools.shop".toUri()
                    }
                    context.startActivity(intent)
                }) {
                    Icon(
                        Icons.Default.Report,
                        contentDescription = stringResource(id = R.string.send_report_email)
                    )
                }
            }
        },
        showBackButton = true,
        onBackClick = onBackClick
    )
}