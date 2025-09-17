package com.loc.searchapp.feature.search.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.core.data.remote.dto.Product
import com.loc.searchapp.feature.shared.components.ProductCardBase
import kotlin.Int

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit,
    onAdd: (id: Int) -> Unit,
    onRemove: (id: Int) -> Unit,
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>
) {

    ProductCardBase(
        modifier = modifier,
        product = product,
        onClick = onClick,
        onAdd = { onAdd(product.id) },
        onRemove = { onRemove(product.id) },
        inProgress = inProgress,
        buttonStates = buttonStates
    )
}