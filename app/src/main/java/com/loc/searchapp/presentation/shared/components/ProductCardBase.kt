package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.EmptyIconSize
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.ImageSize
import com.loc.searchapp.core.ui.values.Dimens.SmallCorner
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.presentation.search.components.ProductDetailRowGrid

@Composable
fun ProductCardBase(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {},
    inProgress: Set<Int>,
    buttonStates: Map<Int, Boolean>,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val imageUrl = "$BASE_URL${product.images}"
    val isInCart = buttonStates[product.id] ?: product.isInCart

    val addMessage = stringResource(id = R.string.added)
    val removeMessage = stringResource(id = R.string.removed)

    val cardDescription: String = stringResource(
        R.string.product_card_description,
        product.shape,
        product.code,
        product.dimensions
    )

    Card(
        modifier = modifier
            .semantics {
                contentDescription = cardDescription
                role = Role.Button
            }
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(StrongCorner),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (isInCart) {
            BorderStroke(SmallCorner, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(
                ExtraSmallCorner,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = BasePadding),
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    shape = RoundedCornerShape(SmallCorner),
                    border = BorderStroke(
                        ExtraSmallCorner,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ,
                    color = Color.Transparent
                ) {
                    Text(
                        text = product.code,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(horizontal = SmallPadding, vertical = ExtraSmallPadding)
                            .clearAndSetSemantics { },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(EmptyIconSize),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(ImageSize)
                        .clip(MaterialTheme.shapes.small),
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .error(R.drawable.placeholder_image)
                        .fallback(R.drawable.placeholder_image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding),
                verticalArrangement = Arrangement.spacedBy(SmallPadding)
            ) {
                Text(
                    text = product.shape,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clearAndSetSemantics { }
                )

                ProductDetailRowGrid(
                    label = stringResource(id = R.string.dimensions),
                    value = product.dimensions
                )

                if (product.nameBonds.isNotEmpty()) {
                    ProductDetailRowGrid(
                        label = stringResource(id = R.string.bond),
                        value = product.nameBonds.joinToString(", ")
                    )
                }

                if (product.gridSize.isNotBlank()) {
                    ProductDetailRowGrid(
                        label = stringResource(id = R.string.grid_size),
                        value = product.gridSize
                    )
                }

                if (product.mounting?.mm?.isNotBlank() == true) {
                    ProductDetailRowGrid(
                        label = stringResource(id = R.string.fit),
                        value = product.mounting.mm
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CartActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    isInCart = isInCart,
                    isInProgress = product.id in inProgress,
                    onAddToCart = {
                        onAdd()
                        onShowSnackbar(addMessage)
                    },
                    onRemoveFromCart = {
                        onRemove()
                        onShowSnackbar(removeMessage)
                    }
                )
            }
        }
    }
}