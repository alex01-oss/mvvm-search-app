package com.loc.searchapp.presentation.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.common.components.ProductsList
import com.loc.searchapp.presentation.common.components.SearchBar
import com.loc.searchapp.presentation.common.components.SharedTopBar
import kotlinx.coroutines.launch

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<Product>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Product) -> Unit,
    onAuthClick: () -> Unit,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    onBackClick: () -> Unit
) {
    val localCartChanges = productViewModel.localCartChanges.collectAsState().value
    val authState = authViewModel.authState.collectAsState().value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mustLoginMessage = stringResource(id = R.string.must_login)
    val loginActionLabel = stringResource(id = R.string.login)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.catalog),
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        content = { paddingValues ->
            Column(
                modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(start = MediumPadding1, top = MediumPadding1, end = MediumPadding1)
            ) {
                SearchBar(
                    text = "",
                    readOnly = false,
                    onValueChange = {},
                    onClick = { navigateToSearch() },
                    onSearch = {},
                    placeholder = stringResource(id = R.string.search_placeholder),
                )

                ProductsList(
                    items = products,
                    onClick = { product -> navigateToDetails(product) },
                    onAdd = { product ->
                        if (authState !is AuthState.Authenticated) {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = mustLoginMessage,
                                    actionLabel = loginActionLabel,
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onAuthClick()
                                }
                            }
                        } else {
                            productViewModel.addToCart(product.code)
                        }
                    },
                    onRemove = { product -> productViewModel.removeFromCart(product.code) },
                    localCartChanges = localCartChanges,
                    showShimmerOnFirstLoad = true,
                )
            }
        }
    )
}