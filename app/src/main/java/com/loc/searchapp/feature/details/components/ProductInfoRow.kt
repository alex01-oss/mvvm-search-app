package com.loc.searchapp.feature.details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.loc.searchapp.core.ui.values.Dimens.EmptyIconSize
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding

@Composable
fun ProductInfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = ExtraSmallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(EmptyIconSize),
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Text(
            text = value,
            style = if (isHighlighted)
                MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            else
                MaterialTheme.typography.bodyMedium,
            color = if (isHighlighted)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}