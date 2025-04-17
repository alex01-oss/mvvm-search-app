package com.loc.searchapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.common.ProductsList
import com.loc.searchapp.presentation.common.SearchBar
import com.loc.searchapp.presentation.home.HomeViewModel

@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigateToDetails: (Product) -> Unit,
    homeViewModel: HomeViewModel
) {
    val lazyProducts = state.products.collectAsLazyPagingItems()
    val localCartChanges = homeViewModel.localCartChanges.collectAsState().value

    Column(
        modifier = Modifier
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
            .statusBarsPadding()
            .fillMaxSize()
    ) {


        SearchBar(
            text = state.searchQuery,
            readOnly = false,
            onValueChange = {
                event(SearchEvent.UpdateSearchQuery(it, state.searchType, state.page))
            },
            onSearch = { event(SearchEvent.SearchProducts) }
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        ProductsList(
            items = lazyProducts,
            onClick = { navigateToDetails },
            onAdd = { },
            onRemove = { },
            localCartChanges = localCartChanges,
        )
    }
}