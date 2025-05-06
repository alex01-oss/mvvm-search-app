package com.loc.searchapp.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R

@Composable
fun AppDialog(
    title: String? = null,
    message: String,
    confirmLabel: String = stringResource(id = R.string.ok),
    onConfirm: () -> Unit,
    dismissLabel: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        title = title?.let { { Text(it) } },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmLabel)
            }
        },
        dismissButton = dismissLabel?.let {
            {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(it)
                }
            }
        }
    )
}