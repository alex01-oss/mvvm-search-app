package com.loc.searchapp.presentation.product_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.ArticleImageHeight
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.product_details.components.DetailsTopBar
import com.loc.searchapp.utils.Constants.CATALOG_URL

@Composable
fun DetailsScreen(
    product: Product,
    event: (DetailsEvent) -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val imageUrl = "$CATALOG_URL${product.images}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailsTopBar(
            onToggleCart = { event(DetailsEvent.UpsertDeleteProduct(product)) },
            onBackClick = navigateUp
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
        ) {
            item {
                AsyncImage(
                    model = ImageRequest
                        .Builder(context = context)
                        .data(imageUrl)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = product.shape,
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(
                        id = R.color.text_title
                    )
                )

                Text(
                    text = product.dimensions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )
            }
        }
    }
}