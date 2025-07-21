package com.loc.searchapp.feature.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.components.lists.ProductsList
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.TopBarPadding
import com.loc.searchapp.feature.search.components.SearchTopSection
import com.loc.searchapp.feature.search.model.SearchEvent
import com.loc.searchapp.feature.search.model.SearchState
import com.loc.searchapp.feature.search.viewmodel.SearchViewModel
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigateToDetails: (Product) -> Unit,
    viewModel: SearchViewModel,
    productViewModel: ProductViewModel,
    onBurgerClick: () -> Unit
) {
    val lazyProducts = viewModel.products.collectAsLazyPagingItems()
    val localCartChanges = productViewModel.localCartChanges.collectAsState().value

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = TopBarPadding, end = MediumPadding1),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SearchTopSection(
                            searchQuery = state.searchQuery,
                            placeholder = state.placeholder,
                            onQueryChange = { query ->
                                event(
                                    SearchEvent.UpdateSearchQuery(
                                        query,
                                        state.searchType,
                                        state.page
                                    )
                                )
                            },
                            onSearch = {
                                event(SearchEvent.SearchProducts)
                            },
                            onBurgerClick = onBurgerClick
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = MediumPadding1)
            ) {
                ProductsList(
                    items = lazyProducts,
                    onClick = navigateToDetails,
                    onAdd = { productViewModel.addToCart(it.code) },
                    onRemove = { productViewModel.removeFromCart(it.code) },
                    localCartChanges = localCartChanges,
                    showLoadingOnEmpty = state.searchQuery.isNotEmpty()
                )
            }
        }
    )
}