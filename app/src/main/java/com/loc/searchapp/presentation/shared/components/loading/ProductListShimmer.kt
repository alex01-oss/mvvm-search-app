package com.loc.searchapp.presentation.shared.components.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.EmptyIconSize
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallCorner
import com.loc.searchapp.core.ui.values.Dimens.ImageSize
import com.loc.searchapp.core.ui.values.Dimens.ShimmerHeight
import com.loc.searchapp.core.ui.values.Dimens.ShimmerHeight3
import com.loc.searchapp.core.ui.values.Dimens.ShimmerHeight4
import com.loc.searchapp.core.ui.values.Dimens.ShimmerWidth
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner


@Composable
fun ProductListShimmer() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = BasePadding),
        verticalArrangement = Arrangement.spacedBy(BasePadding),
        horizontalArrangement = Arrangement.spacedBy(BasePadding)
    ) {
        items(8) {
            ProductCardShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.5f)
            )
        }
    }
}

@Composable
fun ProductCardShimmer(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(StrongCorner),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            ExtraSmallCorner,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = BasePadding),
                horizontalArrangement = Arrangement.Start
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(ShimmerWidth)
                        .height(ShimmerHeight)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(EmptyIconSize),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(ImageSize)
                        .clip(MaterialTheme.shapes.small)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .height(ShimmerHeight3)
                        .fillMaxWidth(0.8f)
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ShimmerHeight4)
                )
            }
        }
    }
}

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                shape = RoundedCornerShape(StrongCorner)
            )
            .shimmerEffect()
    )
}