package com.loc.searchapp.presentation.cart.components

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.ProductCardSize
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.utils.Constants.CATALOG_URL

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onClick: () -> Unit,
    onRemove: (CartItem) -> Unit
) {
    val context = LocalContext.current
    val imageUrl = "$CATALOG_URL${cartItem.product.images}"

    Row(
        modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        AsyncImage(
            modifier = modifier
                .size(ProductCardSize)
                .clip(MaterialTheme.shapes.small),
            model = ImageRequest
                .Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .error(R.drawable.placeholder_image)
                .fallback(R.drawable.placeholder_image)
                .build(),
            contentDescription = null,
        )

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .padding(start = MediumPadding1, end = ExtraSmallPadding)
                .height(ProductCardSize)
                .weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = cartItem.product.code,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.text_title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.shape),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(id = R.color.body),
                )

                Text(
                    text = cartItem.product.shape,
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
                text = cartItem.product.dimensions,
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(id = R.color.body),
            )
        }
        Column(
            modifier
                .align(Alignment.CenterVertically)
                .padding(end = SmallPadding)
        ) {
            IconButton(
                onClick = { onRemove.invoke(cartItem) },
            ) {
                Icon(
                    painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}