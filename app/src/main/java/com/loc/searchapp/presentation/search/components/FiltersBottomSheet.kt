package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.FilterItem
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.presentation.home.components.FilterCategory
import com.loc.searchapp.presentation.shared.model.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    state: UiState<Filters>,
    selectedFilters: Map<String, List<Int>>,
    onFilterToggle: (String, Int, Boolean) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val themeColor = themeAdaptiveColor()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IconSize),
            verticalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.filters),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            }

            when (state) {
                is UiState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                is UiState.Error -> {
                    item {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is UiState.Empty -> {
                    item {
                        Text(
                            text = stringResource(R.string.empty_filters),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is UiState.Success -> {
                    val filtersMap = mutableMapOf<String, List<FilterItem>>()
                    if (state.data.bonds.isNotEmpty()) filtersMap["bonds"] = state.data.bonds
                    if (state.data.grids.isNotEmpty()) filtersMap["grids"] = state.data.grids
                    if (state.data.mountings.isNotEmpty()) filtersMap["mountings"] = state.data.mountings

                    filtersMap.forEach { (categoryKey, items) ->
                        item {
                            FilterCategory(
                                title = categoryKey,
                                items = items,
                                selectedIds = selectedFilters[categoryKey] ?: emptyList(),
                                onToggle = { itemId, isChecked ->
                                    onFilterToggle(categoryKey, itemId, isChecked)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(BasePadding))

                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = stringResource(R.string.apply),
                        tint = themeColor
                    )
                    Spacer(modifier = Modifier.width(SmallPadding))
                    Text(
                        text = stringResource(R.string.apply),
                        color = themeColor
                    )
                }
            }
        }
    }
}