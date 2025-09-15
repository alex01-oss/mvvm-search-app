package com.loc.searchapp.feature.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.core.ui.components.lists.ProductsList
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.search.components.FiltersBottomSheet
import com.loc.searchapp.feature.search.components.SearchBottomSheet
import com.loc.searchapp.feature.search.viewmodel.SearchViewModel
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navigateToDetails: (id: Int) -> Unit,
    viewModel: SearchViewModel,
    productViewModel: ProductViewModel,
) {
    val lazyProducts = viewModel.products.collectAsLazyPagingItems()
    val filtersState by viewModel.filtersState.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()

    var showSearchSheet by remember { mutableStateOf(false) }
    var showFiltersSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.search),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        modifier.size(IconSize)
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = MediumPadding1)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showSearchSheet = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Пошук")
                    }

                    OutlinedButton(
                        onClick = { showFiltersSheet = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Фільтри")
                    }
                }

                ProductsList(
                    items = lazyProducts,
                    onClick = navigateToDetails,
                    onAdd = { productViewModel.addToCart(it.id) },
                    onRemove = { productViewModel.removeFromCart(it.id) },
                )
            }

            if (showSearchSheet) {
                SearchBottomSheet(
//                    searchFields = searchFields,
//                    onSearch = onSearch,
                    onDismiss = { showSearchSheet = false }
                )
            }

            if (showFiltersSheet) {
                FiltersBottomSheet(
                    state = filtersState,
                    selectedFilters = selectedFilters,
                    onFilterToggle = viewModel::toggleFilter,
                    onDismiss = { showFiltersSheet = false }
                )
            }
        }
    )
}