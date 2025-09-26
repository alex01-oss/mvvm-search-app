package com.loc.searchapp.presentation.cart.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.presentation.shared.components.ProductCardBase

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onClick: () -> Unit,
    onRemove: (id: Int) -> Unit,
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>
) {
    ProductCardBase(
        modifier = modifier,
        product = cartItem.product,
        onClick = onClick,
        onRemove = { onRemove(cartItem.product.id) },
        inProgress = inProgress,
        buttonStates = buttonStates
    )
}