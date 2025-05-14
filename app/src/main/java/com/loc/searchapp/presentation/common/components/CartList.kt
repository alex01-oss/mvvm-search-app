package com.loc.searchapp.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.cart.components.CartItemCard


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
                contentPadding = PaddingValues(top = MediumPadding1)
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