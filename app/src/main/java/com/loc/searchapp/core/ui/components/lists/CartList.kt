package com.loc.searchapp.core.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.cart.components.CartItemCard
import com.loc.searchapp.feature.shared.components.ProductListShimmer
import com.loc.searchapp.feature.shared.model.UiState


@Composable
fun CartList(
    state: UiState<List<CartItem>>,
    modifier: Modifier = Modifier,
    onClick: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit,
) {
    when(state) {
        UiState.Empty -> {
            EmptyScreen(message = stringResource(id = R.string.empty_cart))
        }
        is UiState.Error -> {
            EmptyScreen(message = state.message)
        }
        UiState.Loading -> {
            ProductListShimmer()
        }
        is UiState.Success -> {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                contentPadding = PaddingValues(top = MediumPadding1)
            ) {
                items(state.data) { cartItem ->
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