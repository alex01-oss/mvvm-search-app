package com.loc.searchapp.core.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding

@Composable
fun InlineErrorMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    Surface(
        modifier
            .fillMaxWidth()
            .padding(bottom = MediumPadding1),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(MediumPadding1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(id = R.string.error),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(SmallPadding))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}