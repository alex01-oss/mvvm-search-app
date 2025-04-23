package com.loc.searchapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthViewModel
import com.loc.searchapp.presentation.common.ProductsList
import com.loc.searchapp.presentation.common.SearchBar
import com.loc.searchapp.presentation.home.components.HomeTopBar
import com.loc.searchapp.presentation.shared_vm.ProductViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    products: LazyPagingItems<Product>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Product) -> Unit,
) {
    val localCartChanges = productViewModel.localCartChanges.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
            .statusBarsPadding()
    ) {
        HomeTopBar(
            viewModel = authViewModel
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        SearchBar(
            text = "",
            readOnly = false,
            onValueChange = {},
            onClick = { navigateToSearch() },
            onSearch = {},
            placeholder = "Search..."
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        ProductsList(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            items = products,
            onClick = { product -> navigateToDetails(product) },
            onAdd = { product -> productViewModel.addToCart(product.code) },
            onRemove = { product -> productViewModel.removeFromCart(product.code) },
            localCartChanges = localCartChanges,
            showShimmerOnFirstLoad = true
        )
    }
}