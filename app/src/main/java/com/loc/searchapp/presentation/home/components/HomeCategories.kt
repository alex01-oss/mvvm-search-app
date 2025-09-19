package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.presentation.shared.components.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ProductCardSize
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.network.NetworkStatus


@Composable
fun HomeCategories(
    modifier: Modifier = Modifier,
    state: UiState<List<Category>>,
    onCategoryClick: (Int) -> Unit,
    networkStatus: NetworkStatus
) {
    when {
        networkStatus != NetworkStatus.Available -> {
            EmptyScreen(stringResource(R.string.no_internet_connection))
        }

        state == UiState.Empty -> EmptyScreen(stringResource(id = R.string.no_categories))
        state is UiState.Error -> EmptyScreen(stringResource(id = R.string.error))
        state == UiState.Loading -> CategoriesShimmer()
        state is UiState.Success -> {
            Column(modifier.fillMaxWidth()) {
                state.data.forEachIndexed { index, category ->
                    val fullImageUrl = "$BASE_URL${category.imgUrl}"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(ProductCardSize)
                            .clip(RoundedCornerShape(StrongCorner))
                            .clickable { onCategoryClick(category.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = fullImageUrl,
                            contentDescription = category.name,
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (index < state.data.size - 1) {
                        Spacer(modifier = Modifier.height(BasePadding))
                    }
                }
            }
        }
    }
}