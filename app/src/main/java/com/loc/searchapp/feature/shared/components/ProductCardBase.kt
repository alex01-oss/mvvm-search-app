package com.loc.searchapp.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.ProductCardSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding2
import com.loc.searchapp.core.utils.Constants.CATALOG_URL

@Composable
fun ProductCardBase(
    modifier: Modifier = Modifier,
    product: Product,
    isInCart: Boolean = false,
    showCartActions: Boolean = true,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    val context = LocalContext.current
    val imageUrl = "$CATALOG_URL${product.images}"

    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(SmallPadding2))
            .background(MaterialTheme.colorScheme.surface)
            .padding(BasePadding)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(ProductCardSize)
                .clip(MaterialTheme.shapes.small),
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .error(R.drawable.placeholder_image)
                .fallback(R.drawable.placeholder_image)
                .build(),
            contentDescription = null,
        )

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(start = MediumPadding1, end = ExtraSmallPadding)
                .height(ProductCardSize)
                .weight(1f)
        ) {
            Text(
                text = product.code,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.shape),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(id = R.color.body),
                )
                Text(
                    text = product.shape,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.body),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.dimensions),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(id = R.color.body),
                )
            }
            Text(
                text = product.dimensions,
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.body),
            )
        }

        if (showCartActions) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = SmallPadding)
            ) {
                if (isInCart) {
                    IconButton(onClick = onRemove) {
                        Icon(
                            painterResource(id = R.drawable.delete),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    IconButton(onClick = onAdd) {
                        Icon(
                            painterResource(id = R.drawable.add_shopping_cart),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}