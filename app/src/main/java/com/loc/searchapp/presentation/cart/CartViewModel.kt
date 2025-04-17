package com.loc.searchapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun getCart() {
        viewModelScope.launch {
            val token = userPreferences.getToken()
            if (!token.isNullOrBlank()) {
                try {
                    _cartItems.value = catalogUseCases.getCart(token)
                } catch (_: Exception) {
                    _cartItems.value = emptyList()
                }
            }
        }
    }
}