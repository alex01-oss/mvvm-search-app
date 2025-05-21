package com.loc.searchapp.core.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.components.loading.ProductCardShimmerEffect
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.cart.components.CartItemCard
import androidx.compose.foundation.lazy.items


@Composable
fun CartList(
    modifier: Modifier = Modifier,
    items: List<CartItem>,
    onClick: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit,
) {
    val isLoading = false

    when {
        isLoading -> {
            ShimmerEffect()
        }

        items.isEmpty() -> {
            EmptyScreen(message = stringResource(id = R.string.empty_cart))
        }

        else -> {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                contentPadding = PaddingValues(vertical = MediumPadding1)
            ) {
                items(items) { cartItem ->
                    CartItemCard(
                        onClick = { onClick(cartItem) },
                        onRemove = { onRemove(cartItem) },
                        cartItem = cartItem
                    )
                }
            }
        }
    }
}

@Composable
private fun ShimmerEffect() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1)
    ) {
        repeat(10) {
            ProductCardShimmerEffect(modifier = Modifier.padding(horizontal = MediumPadding1))
        }
    }
}