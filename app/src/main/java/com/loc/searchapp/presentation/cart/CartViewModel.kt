package com.loc.searchapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {
    private val _products = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _products.asStateFlow()

    init {
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch {
            _products.value = catalogUseCases.getCart()
        }
    }
}