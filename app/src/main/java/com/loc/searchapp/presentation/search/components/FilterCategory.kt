package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.core.domain.model.catalog.FilterItem
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding

@Composable
fun FilterCategory(
    title: String,
    items: List<FilterItem>,
    selectedIds: List<Int>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            thickness = 1.dp
        )

        items.forEach { item ->
            val displayName = when (item) {
                is FilterItem.Bond -> item.name
                is FilterItem.Grid -> item.size
                is FilterItem.Mounting -> item.mm
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SmallPadding)
                    .semantics(mergeDescendants = true) {
                        role = Role.Checkbox
                    }
                    .clickable {
                        val isCurrentlySelected = selectedIds.contains(item.id)
                        onToggle(item.id, !isCurrentlySelected)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedIds.contains(item.id),
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface,
                        checkmarkColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.clearAndSetSemantics { }
                )
                Spacer(modifier = Modifier.width(SmallPadding))
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}