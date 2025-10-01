package com.loc.searchapp.presentation.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.presentation.shared.components.loading.ProductListShimmer
import com.loc.searchapp.presentation.shared.components.notifications.EmptyContent
import com.loc.searchapp.presentation.shared.model.UiState

@Composable
fun CartList(
    state: UiState<Cart>,
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit,
    onRemove: (id: Int) -> Unit,
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>,
    onShowSnackbar: (String) -> Unit
) {
    when(state) {
        UiState.Empty -> {
            EmptyContent(
                message = stringResource(id = R.string.empty_cart),
                iconId = R.drawable.ic_empty_cart,
            )
        }
        is UiState.Error -> {
            EmptyContent(
                message = state.message,
                iconId = R.drawable.ic_network_error,
            )
        }
        UiState.Loading -> {
            ProductListShimmer()
        }
        is UiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = BasePadding),
                verticalArrangement = Arrangement.spacedBy(BasePadding),
                horizontalArrangement = Arrangement.spacedBy(BasePadding)
            ) {
                items(state.data.cart) { cartItem ->
                    CartItemCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.5f),
                        onClick = { onClick(cartItem.product.id) },
                        onRemove = { onRemove(cartItem.product.id) },
                        cartItem = cartItem,
                        inProgress = inProgress,
                        buttonStates = buttonStates,
                        onShowSnackbar = onShowSnackbar,
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.fillMaxWidth().height(MediumPadding1))
                }
            }
        }
    }
}