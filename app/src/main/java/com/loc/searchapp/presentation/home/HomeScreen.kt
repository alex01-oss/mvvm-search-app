package com.loc.searchapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.loc.searchapp.domain.model.ListItem
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.common.ProductsList
import com.loc.searchapp.presentation.common.SearchBar
import com.loc.searchapp.presentation.home.components.HomeTopBar

@Composable
fun HomeScreen(
    products: State<List<Product>>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Product) -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit
) {

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
        HomeTopBar()

        Spacer(modifier = Modifier.height(MediumPadding1))

        SearchBar(
            text = "",
            readOnly = false,
            onValueChange = {},
            onClick = { navigateToSearch() },
            onSearch = {}
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        val catalogListItems = products.value.map { ListItem.CatalogListItem(it) }

        ProductsList(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            items = catalogListItems,
            onClick = { navigateToDetails },
            onAdd = {  },
            onRemove = {  },
            onDelete = {  }
        )
    }
}
