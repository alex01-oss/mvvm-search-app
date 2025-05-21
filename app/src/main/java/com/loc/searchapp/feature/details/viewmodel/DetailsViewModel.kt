package com.loc.searchapp.feature.details.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.domain.model.catalog.Bond
import com.loc.searchapp.core.domain.model.catalog.DetailsState
import com.loc.searchapp.core.domain.model.catalog.EquipmentModel
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

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
}