package com.loc.searchapp.feature.search.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.loc.searchapp.core.data.remote.dto.FilterItem
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.feature.home.components.FilterCategory
import com.loc.searchapp.feature.shared.model.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    state: UiState<FiltersResponse>,
    selectedFilters: Map<String, List<Int>>,
    onFilterToggle: (String, Int, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фільтри",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити")
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
                            text = "Фільтри відсутні",
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
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Застосувати")
                }
            }
        }
    }
}