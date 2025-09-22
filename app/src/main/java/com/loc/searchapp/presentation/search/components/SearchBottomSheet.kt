package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.SearchParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    currentSearchParams: SearchParams,
    onSearch: (SearchParams) -> Unit,
    onDismiss: () -> Unit,
    suggestions: List<String> = emptyList(),
    isLoadingSuggestion: Boolean = false,
    currentAutocompleteField: String? = null,
    onAutocompleteRequest: (String, String) -> Unit = { _, _ -> },
    onClearAutocomplete: () -> Unit = {}
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchFields by remember { mutableStateOf(currentSearchParams) }

    DisposableEffect(Unit) {
        onDispose {
            onClearAutocomplete()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onClearAutocomplete()
            onDismiss()
        },
        sheetState = bottomSheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.search_params),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    onClearAutocomplete()
                    onDismiss()
                }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
            }

            SearchTextField(
                value = searchFields.searchCode,
                onValueChange = { searchFields = searchFields.copy(searchCode = it) },
                label = stringResource(R.string.enter_code),
                leadingIcon = Icons.Default.Search,
                suggestions = if (currentAutocompleteField === "code") suggestions else emptyList(),
                isLoadingSuggestions = isLoadingSuggestion && currentAutocompleteField === "code",
                onTextChanged = {query -> onAutocompleteRequest(query, "code") },
                onSuggestionClick = { suggestion ->
                    searchFields = searchFields.copy(searchCode = suggestion)
                    onClearAutocomplete()
                },
                fieldType = "code"
            )

            SearchTextField(
                value = searchFields.searchShape,
                onValueChange = { searchFields = searchFields.copy(searchShape = it) },
                label = stringResource(R.string.enter_shape),
                leadingIcon = Icons.Default.Search,
                suggestions = if (currentAutocompleteField === "shape") suggestions else emptyList(),
                isLoadingSuggestions = isLoadingSuggestion && currentAutocompleteField === "shape",
                onTextChanged = {query -> onAutocompleteRequest(query, "shape") },
                onSuggestionClick = { suggestion ->
                    searchFields = searchFields.copy(searchShape = suggestion)
                    onClearAutocomplete()
                },
                fieldType = "shape"
            )

            SearchTextField(
                value = searchFields.searchDimensions,
                onValueChange = { searchFields = searchFields.copy(searchDimensions = it) },
                label = stringResource(R.string.enter_dimensions),
                leadingIcon = Icons.Default.Search,
                suggestions = if (currentAutocompleteField === "dimensions") suggestions else emptyList(),
                isLoadingSuggestions = isLoadingSuggestion && currentAutocompleteField === "dimensions",
                onTextChanged = {query -> onAutocompleteRequest(query, "dimensions") },
                onSuggestionClick = { suggestion ->
                    searchFields = searchFields.copy(searchDimensions = suggestion)
                    onClearAutocomplete()
                },
                fieldType = "dimensions"
            )

            SearchTextField(
                value = searchFields.searchMachine,
                onValueChange = { searchFields = searchFields.copy(searchMachine = it) },
                label = stringResource(R.string.enter_machine),
                leadingIcon = Icons.Default.Search,
                suggestions = if (currentAutocompleteField === "machine") suggestions else emptyList(),
                isLoadingSuggestions = isLoadingSuggestion && currentAutocompleteField === "machine",
                onTextChanged = {query -> onAutocompleteRequest(query, "machine") },
                onSuggestionClick = { suggestion ->
                    searchFields = searchFields.copy(searchMachine = suggestion)
                    onClearAutocomplete()
                },
                fieldType = "machine"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onSearch(searchFields)
                    onClearAutocomplete()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = searchFields.hasAnyValue()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.apply))
            }

            OutlinedButton(
                onClick = {
                    searchFields = SearchParams()
                    onSearch(SearchParams())
                    onClearAutocomplete()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.clear_all))
            }
        }
    }
}