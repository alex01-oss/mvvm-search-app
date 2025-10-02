package com.loc.searchapp.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.presentation.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var isCartScreenActive = false

    fun setCartScreenActive(active: Boolean) {
        isCartScreenActive = active
    }
    private val _cartState = MutableStateFlow<UiState<Cart>>(UiState.Loading)
    val cartState = _cartState.asStateFlow()

    private val _inProgress = MutableStateFlow<Set<Int>>(emptySet())
    val inProgress = _inProgress.asStateFlow()

    private val _buttonStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val buttonStates = _buttonStates.asStateFlow()

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = UiState.Loading

            try {
                val cart = catalogUseCases.getCart()
                _cartState.value = UiState.Success(cart)
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun addToCart(id: Int) {
        viewModelScope.launch {
            _inProgress.update { it + id }
            try {
                val token = userPreferences.getAccessToken()
                if (!token.isNullOrBlank()) {
                    catalogUseCases.addProduct(CatalogId(id))
                    _buttonStates.update { it + (id to true) }
                    if (isCartScreenActive) {
                        loadCart()
                    }
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            } finally {
                _inProgress.update { it - id }
            }
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            _inProgress.update { it + id }

            val currentSuccessState = _cartState.value as? UiState.Success ?: return@launch
            val updatedCart = currentSuccessState.data.cart.filter { it.product.id != id }

            if (updatedCart.isEmpty()) {
                _cartState.value = UiState.Empty
            } else {
                _cartState.value = currentSuccessState.copy(
                    data = currentSuccessState.data.copy(cart = updatedCart)
                )
            }

            try {
                catalogUseCases.deleteProduct(CatalogId(id))
                _buttonStates.update { it + (id to false) }
            } catch (e: Exception) {
                _cartState.value = currentSuccessState
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            } finally {
                _inProgress.update { it - id }
            }
        }
    }
}