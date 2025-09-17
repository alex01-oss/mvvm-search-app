package com.loc.searchapp.feature.cart.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.data.remote.dto.CartItem
import com.loc.searchapp.feature.shared.components.ProductCardBase

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