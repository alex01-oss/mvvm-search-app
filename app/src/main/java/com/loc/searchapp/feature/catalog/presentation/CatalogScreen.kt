package com.loc.searchapp.feature.catalog.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.components.common.AppSnackbar
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.core.ui.components.lists.ProductsList
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<Product>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Product) -> Unit,
    productViewModel: ProductViewModel,
    onBackClick: () -> Unit
) {
    val localCartChanges by productViewModel.localCartChanges.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val addMessage = stringResource(id = R.string.added)
    val removeMessage = stringResource(id = R.string.removed)

    fun showSnackbarImmediately(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data -> AppSnackbar(data) }
            )
        },
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.catalog),
                showBackButton = true,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { navigateToSearch() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null,
                            modifier = Modifier.size(IconSize)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = MediumPadding1)
            ) {
                ProductsList(
                    items = products,
                    onClick = { product -> navigateToDetails(product) },
                    onAdd = { product ->
                        productViewModel.addToCart(product.code)
                        scope.launch { showSnackbarImmediately(addMessage) }
                    },
                    onRemove = { product ->
                        productViewModel.removeFromCart(product.code)
                        scope.launch { snackbarHostState.showSnackbar(message = removeMessage) }
                    },
                    localCartChanges = localCartChanges,
                )
            }
        }
    )
}