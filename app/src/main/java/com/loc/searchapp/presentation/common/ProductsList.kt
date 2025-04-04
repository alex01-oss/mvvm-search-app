package com.loc.searchapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.loc.searchapp.domain.model.ListItem
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.cart.components.CartItemCard
import com.loc.searchapp.presentation.home.components.ProductCard

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    items: List<ListItem>,
    onClick: (ListItem) -> Unit,
    onAdd: (ListItem) -> Unit,
    onRemove: (ListItem) -> Unit,
    onDelete: (ListItem) -> Unit
) {
    val isLoading = false

    if (isLoading) {
        ShimmerEffect()
    } else if (items.isEmpty()) {
        EmptyScreen()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            items(items.size) { index ->
                when (val item = items[index]) {
                    is ListItem.CartListItem -> CartItemCard(
                        cartItem = item.cartItem,
                        onClick = { onClick(item) },
                        onDelete = {  }
                    )

                    is ListItem.CatalogListItem -> ProductCard(
                        product = item.product,
                        onClick = { onClick(item) },
                        onAdd = {  },
                        onRemove = {  },
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
