package com.loc.searchapp.presentation.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppSnackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = snackbarHostState,
    ) { data ->
        Snackbar(
            modifier = modifier.padding(16.dp),
            action = {
                if (data.visuals.actionLabel != null) {
                    TextButton(
                        onClick = { data.performAction() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    ) {
                        Text(data.visuals.actionLabel!!)
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            dismissAction = {
                IconButton(onClick = { data.dismiss() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(data.visuals.message)
            }
        }
    }
}