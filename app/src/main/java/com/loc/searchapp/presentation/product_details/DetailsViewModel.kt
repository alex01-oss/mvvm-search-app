package com.loc.searchapp.presentation.product_details

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.domain.model.Bond
import com.loc.searchapp.domain.model.DetailsState
import com.loc.searchapp.domain.model.EquipmentModel
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteProduct -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val product = catalogUseCases.selectProduct(code = event.product.code)

                    if (product == null) {
                        upsertArticle(code = event.product.code)
                    } else {
                        deleteArticle(code = event.product.code)
                    }
                }
            }

            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    fun loadProduct(code: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val response = catalogUseCases.getCatalogItem(code = code)
                val (prod, bnd, mchs) = response.toDomain()
                _state.value = _state.value.copy(
                    isLoading = false,
                    product = prod,
                    bond = bnd,
                    machines = mchs
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun CatalogItemDetailedResponse.toDomain(): Triple<Product, Bond?, List<EquipmentModel>> {
        val product = Product(
            code = item.code,
            shape = item.shape.orEmpty(),
            dimensions = item.dimensions.orEmpty(),
            images = item.images.orEmpty(),
            nameBond = item.nameBond.orEmpty(),
            gridSize = item.gridSize.orEmpty(),
            isInCart = item.isInCart
        )

        val bond = bond?.let {
            Bond(
                nameBond = it.nameBond,
                bondDescription = it.bondDescription,
                bondCooling = it.bondCooling
            )
        }

        val machines = machines.map {
            EquipmentModel(
                nameEquipment = it.nameEquipment,
                producerName = it.nameProducer
            )
        }

        return Triple(product, bond, machines)
    }

    private suspend fun upsertArticle(code: String) {
        catalogUseCases.addProduct(code = code)
        sideEffect = "Product inserted"
    }

    private suspend fun deleteArticle(code: String) {
        catalogUseCases.deleteProduct(code = code)
        sideEffect = "Product deleted"
    }
}