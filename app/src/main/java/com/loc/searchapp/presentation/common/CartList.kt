package com.loc.searchapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.cart.components.CartItemCard
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
            EmptyScreen(error = Throwable("Cart is empty"))
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                contentPadding = PaddingValues(all = ExtraSmallPadding2)
            ) {
                items(items) { cartItem ->
                    CartItemCard(
                        item = cartItem,
                        onClick = { onClick(cartItem) },
                        onRemove = { onRemove(cartItem) }
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