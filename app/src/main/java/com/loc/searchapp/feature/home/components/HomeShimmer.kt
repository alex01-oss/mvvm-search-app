package com.loc.searchapp.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.loc.searchapp.feature.shared.components.shimmerEffect
import com.loc.searchapp.core.ui.values.Dimens.ArticleImageHeight
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.DefaultCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding2
import com.loc.searchapp.core.ui.values.Dimens.IndicatorSize
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.ProductCardSize
import com.loc.searchapp.core.ui.values.Dimens.ShimmerHeight2
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.ui.values.Dimens.TextBarHeight

@Composable
fun CategoriesShimmer(
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        repeat(2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ProductCardSize)
                    .padding(vertical = ExtraSmallPadding)
                    .clip(RoundedCornerShape(DefaultCorner))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun PostsSliderShimmer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(ArticleImageHeight)
            .clip(RoundedCornerShape(StrongCorner))
            .shimmerEffect()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(TextBarHeight)
                .background(MaterialTheme.colorScheme.background)
                .shimmerEffect()
                .padding(horizontal = BasePadding, vertical = SmallPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(ShimmerHeight2)
                    .clip(RoundedCornerShape(StrongCorner))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(ExtraSmallPadding2))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(IndicatorSize)
                    .clip(RoundedCornerShape(StrongCorner))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun VideoSliderShimmer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(PostImageHeight)
            .clip(RoundedCornerShape(StrongCorner))
            .shimmerEffect(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(48.dp)
        )
    }
}
