package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loc.searchapp.data.remote.dto.MenuCategory
import com.loc.searchapp.data.remote.dto.MenuResponse


@Composable
fun HomeCategories(
    modifier: Modifier = Modifier,
    menu: MenuResponse?,
    onCategoryClick: (MenuCategory) -> Unit
) {
    val categories = remember(menu) {
        menu?.getAllCategories().orEmpty()
    }

    var selectedCategory by remember { mutableStateOf<MenuCategory?>(null) }

    if (categories.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Transparent)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No categories yet",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        return
    }

    Column(modifier.fillMaxWidth()) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .border(
                        width = if (isSelected) 0.dp else 1.dp,
                        color =
                            if (isSelected) Color.Transparent
                            else MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        selectedCategory = category
                        onCategoryClick(category)
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.title.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        if (isSelected) Color.White
                        else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

fun MenuResponse.getAllCategories(): List<MenuCategory> {
    return listOf(
        sharpeningTool,
        axialTool,
        grindingTool,
        constructionTool
    )
}