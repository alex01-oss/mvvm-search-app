package com.loc.searchapp.feature.cart.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.feature.shared.components.ProductCardBase

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onClick: () -> Unit,
    onRemove: (id: Int) -> Unit
) {
    ProductCardBase(
        modifier = modifier,
        product = cartItem.product,
        isInCart = true,
        onClick = onClick,
        onRemove = { onRemove(cartItem.product.id) },
        showCartActions = true
    )
}