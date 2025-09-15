package com.loc.searchapp.feature.product_details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.DetailsData
import com.loc.searchapp.core.ui.components.common.AppSnackbar
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.core.ui.values.Dimens.ArticleImageHeight
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.feature.product_details.components.EquipmentRow
import com.loc.searchapp.feature.product_details.components.ProductInfoRow
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: UiState<DetailsData>,
    onBackClick: () -> Unit,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val addMessage = stringResource(id = R.string.added)
    val removeMessage = stringResource(id = R.string.removed)

    val product = (state as? UiState.Success)?.data?.product

    fun showSnackbarImmediately(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            if (product != null) {
                SharedTopBar(
                    title = stringResource(id = R.string.product_info),
                    onBackClick = onBackClick,
                    showBackButton = true,
                    actions = {
                        if (product.isInCart) {
                            IconButton(onClick = {
                                productViewModel.removeFromCart(product.id)
                                scope.launch { showSnackbarImmediately(removeMessage) }
                            }) {
                                Icon(
                                    painterResource(id = R.drawable.delete),
                                    contentDescription = stringResource(id = R.string.delete_from_cart),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                productViewModel.addToCart(product.id)
                                scope.launch { showSnackbarImmediately(addMessage) }
                            }) {
                                Icon(
                                    painterResource(id = R.drawable.add_shopping_cart),
                                    contentDescription = stringResource(id = R.string.add_to_cart),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data -> AppSnackbar(data) }
            )
        }
    ) { paddingValues ->
        val modifierWithPadding = modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())

        when (state) {
            UiState.Loading -> Box(modifierWithPadding, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }

            is UiState.Error -> Box(modifierWithPadding, contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}")
            }

            UiState.Empty -> Box(modifierWithPadding, contentAlignment = Alignment.Center) {
                Text(text = "Product is empty")
            }

            is UiState.Success -> {
                val data = state.data
                val imageUrl = "$BASE_URL${data.product.images}"

                LazyColumn(
                    modifier = modifierWithPadding,
                    contentPadding = PaddingValues(horizontal = MediumPadding1, vertical = MediumPadding1)
                ) {
                    fun card(content: @Composable ColumnScope.() -> Unit) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = MediumPadding1),
                                elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(Modifier.padding(MediumPadding1), content = content)
                            }
                        }
                    }

                    card {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imageUrl).build(),
                            contentDescription = data.product.code,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(ArticleImageHeight)
                        )
                    }

                    card {
                        Text(
                            text = stringResource(id = R.string.product_details),
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.light_red),
                            modifier = Modifier.padding(bottom = SmallPadding)
                        )

                        @Composable
                        fun info(label: String, value: String) {
                            ProductInfoRow(label = label, value = value)
                            HorizontalDivider(modifier = Modifier.padding(vertical = ExtraSmallPadding))
                        }

                        info(stringResource(id = R.string.code), data.product.code)
                        info(stringResource(id = R.string.shape), data.product.shape)
                        info(stringResource(id = R.string.dimensions), data.product.dimensions)
//                        info(stringResource(id = R.string.bond), data.product.nameBonds)
                        info(stringResource(id = R.string.grid_size), data.product.gridSize)
                    }

                    data.bond?.let { bond ->
                        card {
                            Text(
                                text = stringResource(id = R.string.bond_info),
                                style = MaterialTheme.typography.titleLarge,
                                color = colorResource(id = R.color.light_red),
                                modifier = Modifier.padding(bottom = SmallPadding)
                            )

                            Text(
                                text = bond.bondDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (bond.bondCooling.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = ExtraSmallPadding))

                                Text(
                                    text = stringResource(id = R.string.cooling_instructions),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = colorResource(id = R.color.light_red),
                                    modifier = Modifier.padding(top = SmallPadding, bottom = ExtraSmallPadding)
                                )

                                Text(
                                    text = bond.bondCooling,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    if (data.machines.isNotEmpty()) {
                        card {
                            Text(
                                text = stringResource(id = R.string.compatible_machines),
                                style = MaterialTheme.typography.titleLarge,
                                color = colorResource(id = R.color.light_red),
                                modifier = Modifier.padding(bottom = SmallPadding)
                            )

                            data.machines.forEachIndexed { index, machine ->
                                EquipmentRow(machine.nameEquipment, machine.producerName)
                                if (index < data.machines.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = ExtraSmallPadding))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}