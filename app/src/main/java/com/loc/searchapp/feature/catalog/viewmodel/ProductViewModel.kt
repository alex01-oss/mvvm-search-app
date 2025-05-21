package com.loc.searchapp.feature.catalog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
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

    fun addToCart(code: String) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getAccessToken()
                if (!token.isNullOrBlank()) {
                    catalogUseCases.addProduct(code)
                    _localCartChanges.value = _localCartChanges.value + (code to true)
                    _cartModified.value = true
                }
            } catch (e: Exception) {
                Log.e("Cart", "Exception: ${e.message}")
            }
        }
    }

    fun removeFromCart(code: String) {
        viewModelScope.launch {
            catalogUseCases.deleteProduct(code)
            _localCartChanges.value = _localCartChanges.value + (code to false)
            _cartModified.value = true
        }
    }

    fun getCart() {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken()
            if (!token.isNullOrBlank()) {
                try {
                    _cartItems.value = catalogUseCases.getCart(token)
                    _cartModified.value = false
                } catch (_: Exception) {
                    _cartItems.value = emptyList()
                    _cartModified.value = false
                }
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