package com.loc.searchapp.presentation.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.NavBarHeight
import com.loc.searchapp.presentation.shared.components.loading.ProductListShimmer
import com.loc.searchapp.presentation.shared.components.notifications.EmptyScreen
import com.loc.searchapp.presentation.shared.model.UiState


@Composable
fun CartList(
    state: UiState<Cart>,
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit,
    onRemove: (id: Int) -> Unit,
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>
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
                items(state.data.cart) { cartItem ->
                    CartItemCard(
                        onClick = { onClick(cartItem.product.id) },
                        onRemove = { onRemove(cartItem.product.id) },
                        cartItem = cartItem,
                        inProgress = inProgress,
                        buttonStates = buttonStates
                    )
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().height(NavBarHeight))
                }
            }
        }
    }
}