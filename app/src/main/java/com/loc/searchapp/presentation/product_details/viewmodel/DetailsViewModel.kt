package com.loc.searchapp.presentation.product_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.ProductDetails
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.presentation.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _detailsState = MutableStateFlow<UiState<ProductDetails>>(UiState.Loading)
    val detailsState = _detailsState.asStateFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _detailsState.value = UiState.Loading

            try {
                val product = catalogUseCases.getCatalogItem(CatalogId(id))
                _detailsState.value = UiState.Success(product)
            } catch (e: Exception) {
                _detailsState.value = UiState.Error(e.message ?: "Error loading details")
            }
        }
    }
}