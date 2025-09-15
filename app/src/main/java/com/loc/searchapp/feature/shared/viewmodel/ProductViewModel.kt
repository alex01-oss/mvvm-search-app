package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.feature.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _cartState = MutableStateFlow<UiState<List<CartItem>>>(UiState.Loading)
    val cartState = _cartState.asStateFlow()

    init {
        viewModelScope.launch {
            _cartState.value = UiState.Loading

            try {
                val items = catalogUseCases.getCart()
                _cartState.value =
                    if (items.isEmpty()) UiState.Empty
                    else UiState.Success(items)

            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun addToCart(id: Int) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getAccessToken()
                if (!token.isNullOrBlank()) catalogUseCases.addProduct(id)
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            try {
                catalogUseCases.deleteProduct(id)
                val currentState = _cartState.value
                if (currentState is UiState.Success) {
                    val updatedItems = currentState.data.filterNot { it.product.id == id }
                    _cartState.value =
                        if (updatedItems.isEmpty()) UiState.Empty
                        else UiState.Success(updatedItems)
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}