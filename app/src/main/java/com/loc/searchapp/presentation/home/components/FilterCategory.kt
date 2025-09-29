package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    .clickable {
                        val isCurrentlySelected = selectedIds.contains(item.id)
                        onToggle(item.id, !isCurrentlySelected)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedIds.contains(item.id),
                    onCheckedChange = { onToggle(item.id, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurface,
                        checkmarkColor = MaterialTheme.colorScheme.onBackground
                    )
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