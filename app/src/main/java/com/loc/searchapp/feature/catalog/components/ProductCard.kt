package com.loc.searchapp.feature.catalog.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.feature.shared.components.ProductCardBase

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    localCartChanges: Map<String, Boolean>,
    onClick: () -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit,
) {
    val isInCart = localCartChanges[product.code] ?: product.isInCart

    ProductCardBase(
        modifier = modifier,
        product = product,
        isInCart = isInCart,
        onClick = onClick,
        onAdd = { onAdd(product) },
        onRemove = { onRemove(product) },
        showCartActions = true
    )
}