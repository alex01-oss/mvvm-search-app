package com.loc.searchapp.feature.product_details.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.domain.model.catalog.Bond
import com.loc.searchapp.core.domain.model.catalog.DetailsData
import com.loc.searchapp.core.domain.model.catalog.EquipmentModel
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.feature.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _detailsState = mutableStateOf<UiState<DetailsData>>(UiState.Loading)
    val detailsState: State<UiState<DetailsData>> = _detailsState

    fun loadProduct(code: String) {
        viewModelScope.launch {
            _detailsState.value = UiState.Loading

            try {
                val response = catalogUseCases.getCatalogItem(code)

                val product = Product(
                    code = response.item.code,
                    shape = response.item.shape.orEmpty(),
                    dimensions = response.item.dimensions.orEmpty(),
                    images = response.item.images.orEmpty(),
                    nameBond = response.item.nameBond.orEmpty(),
                    gridSize = response.item.gridSize.orEmpty(),
                    isInCart = response.item.isInCart
                )

                val bond = response.bond?.let {
                    Bond(
                        nameBond = it.nameBond,
                        bondDescription = it.bondDescription,
                        bondCooling = it.bondCooling
                    )
                }

                val machines = response.machines.map {
                    EquipmentModel(
                        nameEquipment = it.nameEquipment,
                        producerName = it.nameProducer
                    )
                }

                if (product.code.isBlank()) {
                    _detailsState.value = UiState.Empty
                } else {
                    _detailsState.value = UiState.Success(
                        DetailsData(product, bond, machines)
                    )
                }
            } catch (e: Exception) {
                _detailsState.value = UiState.Error(e.message ?: "Error loading details")
            }
        }
    }
}