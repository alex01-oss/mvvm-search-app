package com.loc.searchapp.presentation.product_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.remote.dto.DetailsData
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.presentation.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _detailsState = MutableStateFlow<UiState<DetailsData>>(UiState.Loading)
    val detailsState: StateFlow<UiState<DetailsData>> = _detailsState.asStateFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _detailsState.value = UiState.Loading

            try {
                val res = catalogUseCases.getCatalogItem(id)

                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null) {
                        _detailsState.value = UiState.Success(body)
                    } else {
                        _detailsState.value = UiState.Empty
                    }
                } else {
                    _detailsState.value = UiState.Error("Error loading details: ${res.code()}")
                }
            } catch (e: Exception) {
                _detailsState.value = UiState.Error(e.message ?: "Error loading details")
            }
        }
    }
}