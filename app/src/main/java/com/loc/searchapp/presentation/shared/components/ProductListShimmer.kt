package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.ProductCardSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding2


@Composable
fun ProductListShimmer() {
    Column(
        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        modifier = Modifier.fillMaxSize().padding(vertical = MediumPadding1)
    ) {
        repeat(8) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(SmallPadding2))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(BasePadding)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(ProductCardSize)
                        .clip(MaterialTheme.shapes.small)
                )

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .padding(start = MediumPadding1, end = ExtraSmallPadding)
                        .height(ProductCardSize)
                        .weight(1f)
                ) {
                    ShimmerBox(modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(0.7f))
                    ShimmerBox(modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.5f))
                    ShimmerBox(modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.4f))
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = SmallPadding)
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
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
                shape = RoundedCornerShape(4.dp)
            )
            .shimmerEffect()
    )
}