package com.loc.searchapp.feature.home.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.core.data.remote.dto.MenuCategory
import com.loc.searchapp.core.data.remote.dto.MenuResponse
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.CategoryHeight
import com.loc.searchapp.core.ui.values.Dimens.DefaultCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding


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
                .height(CategoryHeight)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Transparent)
                .border(
                    width = ExtraSmallCorner,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(DefaultCorner)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.no_categories),
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
                    .padding(vertical = ExtraSmallPadding)
                    .clip(RoundedCornerShape(DefaultCorner))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .border(
                        width = if (isSelected) 0.dp else ExtraSmallCorner,
                        color =
                            if (isSelected) Color.Transparent
                            else MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(DefaultCorner)
                    )
                    .clickable {
                        selectedCategory = category
                        onCategoryClick(category)
                    }
                    .padding(BasePadding),
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