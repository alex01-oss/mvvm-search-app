package com.loc.searchapp.presentation.product_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = stringResource(id = R.string.detailed),
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(id = R.color.text_title)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.code),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(id = R.color.body),
                    )

                    Text(
                        text = product.code,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.text_title),
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.shape),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(id = R.color.body),
                    )

                    Text(
                        text = product.shape,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.body),
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.dimensions),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(id = R.color.body),
                    )

                    Text(
                        text = product.dimensions,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.body),
                    )
                }
            }
        }
    }
}