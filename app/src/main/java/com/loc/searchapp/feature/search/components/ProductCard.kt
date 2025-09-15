package com.loc.searchapp.feature.search.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.feature.shared.components.ProductCardBase

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit,
    onAdd: (id: Int) -> Unit,
    onRemove: (id: Int) -> Unit,
) {

    ProductCardBase(
        modifier = modifier,
        product = product,
        isInCart = product.isInCart,
        onClick = onClick,
        onAdd = { onAdd(product.id) },
        onRemove = { onRemove(product.id) },
        showCartActions = true
    )
}