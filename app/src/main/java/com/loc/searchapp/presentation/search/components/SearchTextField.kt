package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.AboutTextWidth
import com.loc.searchapp.core.ui.values.Dimens.BorderStroke
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.IndicatorSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    suggestions: List<String> = emptyList(),
    isLoadingSuggestions: Boolean = false,
    onSuggestionClick: (String) -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    fieldType: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(suggestions) {
        expanded = suggestions.isNotEmpty()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                onTextChanged(newValue)
                expanded = true
            },
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (isLoadingSuggestions) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(IconSize),
                        strokeWidth = BorderStroke
                    )
                } else if (suggestions.isNotEmpty()) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.suggestions),
                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor()
            ,
            singleLine = true,
            shape = RoundedCornerShape(IndicatorSize)
        )

        if (suggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = AboutTextWidth)
            ) {
                suggestions.take(5).forEach { suggestion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onSuggestionClick(suggestion)
                            onValueChange(suggestion)
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(IndicatorSize),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    }
}