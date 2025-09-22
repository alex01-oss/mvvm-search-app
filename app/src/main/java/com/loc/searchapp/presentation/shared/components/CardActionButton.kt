package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R

@Composable
fun CartActionButton(
    isInCart: Boolean,
    isInProgress: Boolean,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    IconButton(
        onClick = if (isInCart) onRemoveFromCart else onAddToCart
    ) {
        when {
            isInProgress -> CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            isInCart -> Icon(
                painterResource(id = R.drawable.delete),
                contentDescription = stringResource(id = R.string.delete_from_cart),
                tint = MaterialTheme.colorScheme.onSurface
            )
            else -> Icon(
                painterResource(id = R.drawable.add_shopping_cart),
                contentDescription = stringResource(id = R.string.add_to_cart),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}