package com.loc.searchapp.presentation.cart

import com.loc.searchapp.domain.model.CartItem

data class CartState(
    val cartItems: List<CartItem> = emptyList()
)

