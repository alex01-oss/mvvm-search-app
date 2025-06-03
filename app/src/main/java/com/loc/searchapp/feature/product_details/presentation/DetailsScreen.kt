package com.loc.searchapp.feature.product_details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.DetailsData
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.core.ui.values.Dimens.ArticleImageHeight
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.utils.Constants.CATALOG_URL
import com.loc.searchapp.feature.product_details.components.EquipmentRow
import com.loc.searchapp.feature.product_details.components.ProductInfoRow
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: UiState<DetailsData>,
    onBackClick: () -> Unit,
    localCartChanges: Map<String, Boolean>,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current

    when (state) {
        UiState.Empty -> EmptyScreen("Product is empty")
        is UiState.Error -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}")
            }
        }

        UiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        is UiState.Success -> {
            val product = state.data.product
            val bond = state.data.bond
            val machines = state.data.machines

            val imageUrl = "$CATALOG_URL${product.images}"
            val isInCart = localCartChanges[product.code] ?: product.isInCart

            Scaffold(
                modifier = modifier,
                containerColor = Color.Transparent,
                topBar = {
                    SharedTopBar(
                        title = stringResource(id = R.string.product_info),
                        onBackClick = onBackClick,
                        actions = {
                            if (isInCart) {
                                IconButton(
                                    onClick = { productViewModel.removeFromCart(product.code) }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.delete),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { productViewModel.addToCart(product.code) },
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.add_shopping_cart),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        },
                        showBackButton = true
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = MediumPadding1,
                            vertical = MediumPadding1
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
                                        value = product.code,
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                    )

                                    ProductInfoRow(
                                        label = stringResource(id = R.string.shape),
                                        value = product.shape
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                    )

                                    ProductInfoRow(
                                        label = stringResource(id = R.string.dimensions),
                                        value = product.dimensions
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                    )

                                    ProductInfoRow(
                                        label = stringResource(id = R.string.bond),
                                        value = product.nameBond
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = ExtraSmallPadding)
                                    )

                                    ProductInfoRow(
                                        label = stringResource(id = R.string.grid_size),
                                        value = product.gridSize
                                    )
                                }
                            }
                        }

                        if (bond != null) {
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
                                            text = bond.bondDescription,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (bond.bondCooling.isNotEmpty()) {
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
                                                text = bond.bondCooling,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (machines.isNotEmpty()) {
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

                                        machines.forEachIndexed { index, machine ->
                                            EquipmentRow(
                                                nameEquipment = machine.nameEquipment,
                                                nameProducer = machine.producerName
                                            )

                                            if (index < machines.size - 1) {
                                                HorizontalDivider(
                                                    modifier = modifier.padding(
                                                        vertical = ExtraSmallPadding
                                                    )
                                                )
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
}