package com.loc.searchapp.core.ui.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
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
                Text(
                    text = confirmLabel,
                    color = colorResource(id = R.color.light_red)
                )
            }
        },
        dismissButton = dismissLabel?.let {
            {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(
                        text = it,
                        color = colorResource(id = R.color.light_red)
                    )
                }
            }
        }
    )
}