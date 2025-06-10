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
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.CategoryHeight
import com.loc.searchapp.core.ui.values.Dimens.DefaultCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.network.NetworkStatus


@Composable
fun HomeCategories(
    modifier: Modifier = Modifier,
    state: UiState<List<MenuCategory>>,
    onCategoryClick: (MenuCategory) -> Unit,
    networkStatus: NetworkStatus
) {
    var selectedCategory by remember { mutableStateOf<MenuCategory?>(null) }

    when {
        networkStatus != NetworkStatus.Available -> {
            EmptyScreen(stringResource(R.string.no_internet_connection))
        }

        state == UiState.Empty -> EmptyScreen(stringResource(id = R.string.no_categories))
        state is UiState.Error -> EmptyScreen(stringResource(id = R.string.error))
        state == UiState.Loading -> CategoriesShimmer()
        state is UiState.Success -> {
            Column(modifier.fillMaxWidth()) {
                state.data.forEach { category ->
                    val isSelected = selectedCategory == category

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(CategoryHeight)
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
    }
}