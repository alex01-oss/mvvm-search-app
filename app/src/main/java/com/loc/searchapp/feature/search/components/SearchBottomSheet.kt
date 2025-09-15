package com.loc.searchapp.feature.search.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.core.utils.SearchParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
//    onSearch: (SearchParams) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchFields by remember { mutableStateOf(SearchParams()) }


    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
                    text = "Параметри пошуку",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрити")
                }
            }

            SearchTextField(
                value = searchFields.searchCode,
                onValueChange = { searchFields = searchFields.copy(searchCode = it) },
                label = "Введіть код...",
                leadingIcon = Icons.Default.Search
            )

            SearchTextField(
                value = searchFields.searchShape,
                onValueChange = { searchFields = searchFields.copy(searchShape = it) },
                label = "Введіть форму...",
                leadingIcon = Icons.Default.Search
            )

            SearchTextField(
                value = searchFields.searchDimensions,
                onValueChange = { searchFields = searchFields.copy(searchDimensions = it) },
                label = "Введіть розміри...",
                leadingIcon = Icons.Default.Search
            )

            SearchTextField(
                value = searchFields.searchMachine,
                onValueChange = { searchFields = searchFields.copy(searchMachine = it) },
                label = "Введіть верстат...",
                leadingIcon = Icons.Default.Search
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
//                    onSearch(searchFields)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Застосувати")
            }

            OutlinedButton(
                onClick = { searchFields = SearchParams() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Очистити все")
            }
        }
    }
}