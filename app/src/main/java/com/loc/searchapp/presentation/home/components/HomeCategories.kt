package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.loc.searchapp.data.remote.dto.MenuCategory
import com.loc.searchapp.data.remote.dto.MenuResponse
import com.loc.searchapp.presentation.Dimens.MediumPadding1


@Composable
fun HomeCategories(
    modifier: Modifier = Modifier,
    menu: MenuResponse?,
    onCategoryClick: () -> Unit
) {
    val categories = remember(menu) {
        menu?.getAllCategories().orEmpty()
    }

    LazyRow(
        modifier
            .fillMaxWidth()
            .padding(vertical = MediumPadding1),
    ) {
        items(categories) {category ->
            CategoryCard(
                title = category.title,
                onCategoryClick = onCategoryClick
            )
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
