package com.loc.searchapp.core.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.catalog.components.ProductCard
import com.loc.searchapp.feature.shared.components.ProductListShimmer
import com.loc.searchapp.feature.shared.model.UiState

@Composable
fun ProductsList(
    state: UiState<Unit>,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Product>,
    onClick: (Product) -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    localCartChanges: Map<String, Boolean>,
) {
    when (state) {
        UiState.Loading -> ProductListShimmer()

        is UiState.Error -> EmptyScreen(message = state.message)

        UiState.Empty -> EmptyScreen(
            message = stringResource(id = R.string.not_found),
            iconId = R.drawable.ic_search_document
        )

        is UiState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                contentPadding = PaddingValues(vertical = MediumPadding1)
            ) {
                items(items.itemCount) { index ->
                    val product = items[index]
                    if (product != null) {
                        ProductCard(
                            product = product,
                            localCartChanges = localCartChanges,
                            onClick = { onClick(product) },
                            onAdd = { onAdd(product) },
                            onRemove = { onRemove(product) },
                        )
                    }
                }

                when (items.loadState.append) {
                    is LoadState.Loading -> item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(24.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    is LoadState.Error -> item {
                        Text(
                            modifier = Modifier.padding(BasePadding),
                            text = stringResource(id = R.string.loading_error),
                            color = Color.Red
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}
