package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.feature.shared.model.UiState
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
    private val _cartState = MutableStateFlow<UiState<CartResponse>>(UiState.Loading)
    val cartState = _cartState.asStateFlow()

    private val _inProgress = MutableStateFlow<Set<Int>>(emptySet())
    val inProgress = _inProgress.asStateFlow()

    private val _buttonStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val buttonStates = _buttonStates.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = UiState.Loading

            try {
                val res = catalogUseCases.getCart()
                if (res.isSuccessful) {
                    val body = res.body()

                    if (body != null) {
                        _cartState.value = UiState.Success(body)
                    } else {
                        _cartState.value = UiState.Empty
                    }
                }

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
                    val res = catalogUseCases.addProduct(id)
                    if (res.isSuccessful) {
                        _buttonStates.update { it + (id to true) }
                        loadCart()
                    } else {
                        _cartState.value = UiState.Error("Не вдалося додати товар")
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
            try {
                val res = catalogUseCases.deleteProduct(id)
                if (res.isSuccessful) {
                    _buttonStates.update { it + (id to false) }
                    loadCart()
                } else {
                    _cartState.value = UiState.Error("Не вдалося видалити товар")
                }
            } catch (e: Exception) {
                _cartState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            } finally {
                _inProgress.update { it - id }
            }
        }
    }

    fun getButtonState(productId: Int, originalIsInCart: Boolean): Boolean {
        return _buttonStates.value[productId] ?: originalIsInCart
    }
}