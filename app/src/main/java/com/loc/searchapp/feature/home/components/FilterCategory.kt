package com.loc.searchapp.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.core.data.remote.dto.FilterItem

@Composable
fun FilterCategory(
    title: String,
    items: List<FilterItem>,
    selectedIds: List<Int>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            thickness = 1.dp
        )

        items.forEach { item ->
            val displayName = when (item) {
                is FilterItem.Bond -> item.nameBond
                is FilterItem.Grid -> item.gridSize
                is FilterItem.Mounting -> item.mm
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val isCurrentlySelected = selectedIds.contains(item.id)
                        onToggle(item.id, !isCurrentlySelected)
                    }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedIds.contains(item.id),
                    onCheckedChange = { onToggle(item.id, it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}