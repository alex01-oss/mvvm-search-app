package com.loc.searchapp.presentation.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.IndicatorSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.presentation.search.components.FiltersBottomSheet
import com.loc.searchapp.presentation.search.components.ProductsList
import com.loc.searchapp.presentation.search.components.SearchBottomSheet
import com.loc.searchapp.presentation.search.viewmodel.SearchViewModel
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navigateToDetails: (Int) -> Unit,
    viewModel: SearchViewModel,
    productViewModel: ProductViewModel,
) {
    val lazyProducts = viewModel.products.collectAsLazyPagingItems()
    val filtersState by viewModel.filtersState.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()

    val searchParams by viewModel.searchParams.collectAsState()
    val suggestions by viewModel.autocompleteSuggestions.collectAsState()
    val isLoadingSuggestions by viewModel.isLoadingAutocomplete.collectAsState()
    val currentAutocompleteField by viewModel.currentAutocompleteField.collectAsState()

    var showSearchSheet by remember { mutableStateOf(false) }
    var showFiltersSheet by remember { mutableStateOf(false) }

    val themeColor = themeAdaptiveColor()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.search),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = stringResource(R.string.search),
                        modifier.size(IconSize)
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = BasePadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = BasePadding),
                    horizontalArrangement = Arrangement.spacedBy(BasePadding)
                ) {
                    Button(
                        onClick = { showSearchSheet = true },
                        shape = RoundedCornerShape(StrongCorner),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search),
                            modifier = Modifier.size(IndicatorSize),
                            tint = themeColor
                        )
                        Spacer(modifier = Modifier.width(SmallPadding))
                        Text(
                            text = stringResource(id = R.string.search),
                            color = themeColor
                        )
                    }

                    OutlinedButton(
                        onClick = { showFiltersSheet = true },
                        shape = RoundedCornerShape(StrongCorner),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(id = R.string.filters),
                            modifier = Modifier.size(IndicatorSize),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(SmallPadding))
                        Text(
                            text = stringResource(id = R.string.filters),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                ProductsList(
                    items = lazyProducts,
                    onClick = { id -> navigateToDetails(id) },
                    onAdd = { id -> productViewModel.addToCart(id) },
                    onRemove = { id -> productViewModel.removeFromCart(id) },
                    inProgress = productViewModel.inProgress.collectAsState().value,
                    buttonStates = productViewModel.buttonStates.collectAsState().value
                )
            }

            if (showSearchSheet) {
                SearchBottomSheet(
                    onSearch = { searchParams -> viewModel.search(searchParams) },
                    onDismiss = { showSearchSheet = false },
                    currentSearchParams = searchParams,
                    suggestions = suggestions,
                    isLoadingSuggestion = isLoadingSuggestions,
                    currentAutocompleteField = currentAutocompleteField,
                    onAutocompleteRequest = { query, fieldType ->
                        viewModel.getAutocomplete(query, fieldType)
                    },
                    onClearAutocomplete = { viewModel.clearAutocomplete() }
                )
            }

            if (showFiltersSheet) {
                FiltersBottomSheet(
                    state = filtersState,
                    selectedFilters = selectedFilters,
                    onFilterToggle = viewModel::toggleFilter,
                    onDismiss = { showFiltersSheet = false },
                    onApply = viewModel::updateFilters,
                )
            }
        }
    )
}