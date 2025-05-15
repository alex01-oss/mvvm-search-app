package com.loc.searchapp.presentation.product_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.DetailsState
import com.loc.searchapp.presentation.Dimens.ArticleImageHeight
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.product_details.components.EquipmentRow
import com.loc.searchapp.presentation.product_details.components.ProductInfoRow
import com.loc.searchapp.presentation.product_details.components.ProductInfoTopBar
import com.loc.searchapp.utils.Constants.CATALOG_URL

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsState,
    event: (DetailsEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val imageUrl = "$CATALOG_URL${state.product?.images}"

    when {
        state.isLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        state.error != null -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}")
            }
        }

        state.product != null -> {
            Column(
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                ProductInfoTopBar(
                    onToggleCart = { event(DetailsEvent.UpsertDeleteProduct(state.product)) },
                    onBackClick = onBackClick,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        top = MediumPadding1,
                        start = MediumPadding1,
                        end = MediumPadding1,
                        bottom = MediumPadding1
                    )
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MediumPadding1),
                            elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(ArticleImageHeight)
                                    .padding(MediumPadding1),
                                model = ImageRequest
                                    .Builder(context = context)
                                    .data(imageUrl)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MediumPadding1),
                            elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(MediumPadding1)
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = SmallPadding),
                                    text = stringResource(id = R.string.product_details),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                ProductInfoRow(
                                    label = stringResource(id = R.string.code),
                                    value = state.product.code,
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                )

                                ProductInfoRow(
                                    label = stringResource(id = R.string.shape),
                                    value = state.product.shape
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                )

                                ProductInfoRow(
                                    label = stringResource(id = R.string.dimensions),
                                    value = state.product.dimensions
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                )

                                ProductInfoRow(
                                    label = stringResource(id = R.string.bond),
                                    value = state.product.nameBond
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                )

                                ProductInfoRow(
                                    label = stringResource(id = R.string.grid_size),
                                    value = state.product.gridSize
                                )
                            }
                        }
                    }

                    if (state.bond != null) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = MediumPadding1),
                                elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MediumPadding1)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(bottom = SmallPadding),
                                        text = stringResource(id = R.string.bond_info),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Text(
                                        modifier = Modifier.padding(bottom = SmallPadding),
                                        text = state.bond.bondDescription,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (state.bond.bondCooling.isNotEmpty()) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                        )

                                        Text(
                                            modifier = Modifier.padding(
                                                top = SmallPadding,
                                                bottom = ExtraSmallPadding
                                            ),
                                            text = stringResource(id = R.string.cooling_instructions),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        Text(
                                            text = state.bond.bondCooling,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (state.machines.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = MediumPadding1),
                                elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MediumPadding1)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.compatible_machines),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = modifier.padding(bottom = SmallPadding)
                                    )

                                    state.machines.forEachIndexed { index, machine ->
                                        EquipmentRow(
                                            nameEquipment = machine.nameEquipment,
                                            nameProducer = machine.producerName
                                        )

                                        if (index < state.machines.size - 1) {
                                            HorizontalDivider(modifier = modifier.padding(vertical = ExtraSmallPadding))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}