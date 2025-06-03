package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.feature.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _localCartChanges = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val localCartChanges = _localCartChanges.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _cartModified = MutableStateFlow(false)
    val cartModified: StateFlow<Boolean> = _cartModified.asStateFlow()

    private val _cartState = MutableStateFlow<UiState<List<CartItem>>>(UiState.Loading)
    val cartState = _cartState.asStateFlow()

    fun addToCart(code: String) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getAccessToken()
                if (!token.isNullOrBlank()) {
                    catalogUseCases.addProduct(code)
                    _localCartChanges.value += (code to true)
                    _cartModified.value = true
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun removeFromCart(code: String) {
        viewModelScope.launch {
            try {
                catalogUseCases.deleteProduct(code)
                _localCartChanges.value += (code to false)
                _cartModified.value = true

                val currentState = _cartState.value
                if (currentState is UiState.Success) {
                    val updatedItems = currentState.data.filterNot { it.product.code == code }
                    _cartState.value = if (updatedItems.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(updatedItems)
                    }
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = UiState.Loading

            try {
                val token = userPreferences.getAccessToken()
                if (token.isNullOrBlank()) {
                    _cartState.value = UiState.Error("Token is empty")
                    return@launch
                }

                val items = catalogUseCases.getCart(token)
                _cartState.value = if (items.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(items)
                }

            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearLocalCart() {
        _localCartChanges.value = emptyMap()
    }

    fun resetCartModified() {
        _cartModified.value = false
    }
}