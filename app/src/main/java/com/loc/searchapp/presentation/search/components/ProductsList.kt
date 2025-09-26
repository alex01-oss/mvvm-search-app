package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallCorner
import com.loc.searchapp.presentation.shared.components.loading.ProductListShimmer
import com.loc.searchapp.presentation.shared.components.notifications.EmptyScreen

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Product>,
    onClick: (Int) -> Unit,
    onAdd: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>
) {
    when (val state = items.loadState.refresh) {
        is LoadState.Error -> {
            val errorMessage = state.error.localizedMessage ?: stringResource(R.string.error)
            EmptyScreen(message = stringResource(R.string.error, errorMessage))
        }

        LoadState.Loading -> ProductListShimmer()

        is LoadState.NotLoading -> {
            if (items.itemCount == 0) {
                EmptyScreen(message = stringResource(id = R.string.not_found))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = BasePadding),
                    verticalArrangement = Arrangement.spacedBy(BasePadding),
                    horizontalArrangement = Arrangement.spacedBy(BasePadding)
                ) {
                    items(items.itemCount) { index ->
                        val product = items[index]
                        if (product != null) {
                            ProductCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.5f),
                                product = product,
                                onClick = { onClick(product.id) },
                                onAdd = { onAdd(product.id) },
                                onRemove = { onRemove(product.id) },
                                inProgress = inProgress,
                                buttonStates = buttonStates
                            )
                        }
                    }

                    when (items.loadState.append) {
                        is LoadState.Loading -> item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(BasePadding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = SmallCorner,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        is LoadState.Error -> item(span = { GridItemSpan(2) }) {
                            Text(
                                modifier = Modifier.padding(BasePadding),
                                text = stringResource(id = R.string.loading_error),
                                color = Color.Red
                            )
                        }

                        else -> Unit
                    }

                    item(span = { GridItemSpan(2) }) {
                        Box(modifier = Modifier.fillMaxWidth().height(MediumPadding1))
                    }
                }
            }
        }
    }
}