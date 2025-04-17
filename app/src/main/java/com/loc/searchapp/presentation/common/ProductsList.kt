package com.loc.searchapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.home.components.ProductCard

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Product>,
    onClick: (Product) -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    localCartChanges: Map<String, Boolean>,
) {
    val loadState = items.loadState

    when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
        }

        loadState.refresh is LoadState.Error -> {
            EmptyScreen(error = (loadState.refresh as LoadState.Error).error)
        }

        loadState.refresh is LoadState.NotLoading && items.itemCount == 0 -> {
            EmptyScreen(
                error = error(
                    message = "error"
                )
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                contentPadding = PaddingValues(all = ExtraSmallPadding2)
            ) {
                items(items.itemCount) { index ->
                    items[index]?.let { product ->
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
                    is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            Text(
                                text = "Loading error",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> Unit
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
