package com.loc.searchapp.core.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Product>,
    onClick: (Product) -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    localCartChanges: Map<String, Boolean>,
    showLoadingOnEmpty: Boolean = true
) {
    when (val state = items.loadState.refresh) {
        is LoadState.Error -> {
            val errorMessage = state.error.localizedMessage ?: stringResource(R.string.error)
            EmptyScreen(message = stringResource(R.string.error, errorMessage))
        }

        LoadState.Loading -> {
            if (showLoadingOnEmpty) {
                ProductListShimmer()
            } else {
                EmptyScreen(message = stringResource(id = R.string.start_searching))
            }
        }

        is LoadState.NotLoading -> {
            if (items.itemCount == 0 ) {
                EmptyScreen(message = stringResource(id = R.string.not_found))
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = MediumPadding1),
                    verticalArrangement = Arrangement.spacedBy(MediumPadding1),
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
}