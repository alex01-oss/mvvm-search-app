package com.loc.searchapp.presentation.product_details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.ProductDetails
import com.loc.searchapp.core.ui.values.Dimens.ArticleImageHeight
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.presentation.product_details.components.EquipmentRow
import com.loc.searchapp.presentation.product_details.components.ProductInfoRow
import com.loc.searchapp.presentation.shared.components.CartActionButton
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.components.notifications.AppSnackbar
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: UiState<ProductDetails>,
    onBackClick: () -> Unit,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showSnackbarAction: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    val addMessage = stringResource(id = R.string.added)
    val removeMessage = stringResource(id = R.string.removed)

    val product = (state as? UiState.Success)?.data?.item

    val inProgress by viewModel.inProgress.collectAsState()
    val buttonStates by viewModel.buttonStates.collectAsState()

    val isInCart = buttonStates[product?.id] ?: product?.isInCart

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                AppSnackbar(data = data)
            }
        },
        topBar = {
            if (product != null) {
                SharedTopBar(
                    title = stringResource(id = R.string.product_info),
                    onBackClick = onBackClick,
                    showBackButton = true,
                    actions = {
                        CartActionButton(
                            isInCart = isInCart == true,
                            isInProgress = product.id in inProgress,
                            onAddToCart = {
                                viewModel.addToCart(product.id)
                                showSnackbarAction(addMessage)
                            },
                            onRemoveFromCart = {
                                viewModel.removeFromCart(product.id)
                                showSnackbarAction(removeMessage)
                            },
                            isDetailedScreen = true
                        )
                    }
                )
            }
        },
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
                val imageUrl = "$BASE_URL${data.item.images}"

                LazyColumn(
                    modifier = modifierWithPadding,
                    contentPadding = PaddingValues(horizontal = BasePadding, vertical = BasePadding)
                ) {
                    fun card(content: @Composable ColumnScope.() -> Unit) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = BasePadding),
                                elevation = CardDefaults.cardElevation(ExtraSmallPadding),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(StrongCorner)
                            ) {
                                Column(Modifier.padding(BasePadding), content = content)
                            }
                        }
                    }

                    card {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imageUrl).build(),
                            contentDescription = data.item.code,
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
                            modifier = Modifier
                                .padding(bottom = BasePadding)
                                .semantics { heading() }
                        )

                        @Composable
                        fun info(label: String, value: String) {
                            ProductInfoRow(label = label, value = value)
                            HorizontalDivider(modifier = Modifier.padding(vertical = ExtraSmallPadding))
                        }

                        info(stringResource(id = R.string.code), data.item.code)
                        info(stringResource(id = R.string.shape), data.item.shape)
                        info(stringResource(id = R.string.dimensions), data.item.dimensions)
                        info(stringResource(id = R.string.grid_size), data.item.gridSize)

                        data.mounting?.let { mounting ->
                            info(stringResource(id = R.string.fit), mounting.mm)
                        }
                    }

                    card {
                        Text(
                            text = stringResource(id = R.string.bond_info),
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.light_red),
                            modifier = Modifier.semantics { heading() }
                        )

                        data.bonds.forEach { bond ->
                            HorizontalDivider(modifier = Modifier.padding(
                                vertical = ExtraSmallPadding)
                            )

                            Text(
                                text = bond.nameBond,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(
                                    top = BasePadding,
                                    bottom = SmallPadding
                                )
                            )

                            Text(
                                text = bond.bondDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (bond.bondCooling.isNotEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.cooling_instructions),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.light_red),
                                    modifier = Modifier.padding(
                                        top = BasePadding,
                                        bottom = ExtraSmallPadding
                                    )
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
                                EquipmentRow(machine.model, machine.name)
                                if (index < data.machines.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(
                                        vertical = ExtraSmallPadding)
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