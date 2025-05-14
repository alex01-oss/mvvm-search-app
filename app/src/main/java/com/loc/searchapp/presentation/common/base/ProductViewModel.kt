package com.loc.searchapp.presentation.common.base

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.domain.model.Bond
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.domain.model.EquipmentModel
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
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

    var cartModified by mutableStateOf(false)
        internal set

    fun addToCart(code: String) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getAccessToken()
                if (!token.isNullOrBlank()) {
                    catalogUseCases.addProduct(code)
                    _localCartChanges.value = _localCartChanges.value + (code to true)
                    cartModified = true
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
            cartModified = true
        }
    }

    fun getCart() {
        viewModelScope.launch {
            val token = userPreferences.getAccessToken()
            if (!token.isNullOrBlank()) {
                try {
                    _cartItems.value = catalogUseCases.getCart(token)
                    cartModified = false
                } catch (_: Exception) {
                    _cartItems.value = emptyList()
                    cartModified = false
                }
            }
        }
    }

    fun clearLocalCart() {
        _localCartChanges.value = emptyMap()
    }
}