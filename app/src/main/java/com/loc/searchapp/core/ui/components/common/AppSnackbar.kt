package com.loc.searchapp.core.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding2

@Composable
fun AppSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier.padding(BasePadding),
        action = {
            data.visuals.actionLabel?.let {
                IconButton(
                    onClick = { data.performAction() },
                    modifier = Modifier.size(IconSize)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = it,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        dismissAction = {
            IconButton(onClick = { data.dismiss() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.dismiss),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = SmallPadding2),
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(id = R.string.info),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(SmallPadding2))
            Text(data.visuals.message)
        }
    }
}