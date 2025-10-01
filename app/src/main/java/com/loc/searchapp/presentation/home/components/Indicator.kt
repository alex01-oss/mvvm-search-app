package com.loc.searchapp.presentation.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.IndicatorSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding

@Composable
fun PagerIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    if (totalPages > 1) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = BasePadding)
                .clearAndSetSemantics { },
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(totalPages) { index ->
                val isSelected = currentPage == index
                Box(
                    modifier = Modifier
                        .size(
                            width = if (isSelected) IndicatorSize else SmallPadding,
                            height = SmallPadding
                        )
                        .background(
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(ExtraSmallPadding)
                        )
                        .animateContentSize()
                )
                if (index < totalPages - 1) {
                    Spacer(modifier = Modifier.width(ExtraSmallPadding))
                }
            }
        }
    }
}