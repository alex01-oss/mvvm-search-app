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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding2
import com.loc.searchapp.core.ui.values.Dimens.IconSize

@Composable
fun CartActionButton(
    modifier: Modifier = Modifier,
    isInCart: Boolean,
    isInProgress: Boolean,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
    isDetailedScreen: Boolean = false
) {
    val themeColor = themeAdaptiveColor()

    val actionLabel = if (isInCart) {
        stringResource(id = R.string.delete_from_cart)
    } else {
        stringResource(id = R.string.add_to_cart)
    }

    IconButton(
        modifier = modifier.semantics { contentDescription = actionLabel },
        onClick = if (isInCart) onRemoveFromCart else onAddToCart,
    ) {
        when {
            isInProgress -> CircularProgressIndicator(
                modifier = Modifier
                    .size(IconSize)
                    .clearAndSetSemantics { },
                strokeWidth = ExtraSmallPadding2,
                color = if (isDetailedScreen) themeColor
                    else MaterialTheme.colorScheme.onBackground
            )
            isInCart -> Icon(
                painterResource(id = R.drawable.delete),
                contentDescription = null,
                tint = if (isDetailedScreen) themeColor
                else MaterialTheme.colorScheme.onBackground
            )
            else -> Icon(
                painterResource(id = R.drawable.add_shopping_cart),
                contentDescription = null,
                tint = if (isDetailedScreen) themeColor
                else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}