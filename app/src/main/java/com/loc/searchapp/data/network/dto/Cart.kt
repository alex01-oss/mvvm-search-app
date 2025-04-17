package com.loc.searchapp.data.network.dto

import com.loc.searchapp.domain.model.CartItem

data class CartResponse(
    val cart: List<CartItem>
)

data class ItemCartResponse(
    val message: String
)

data class ItemCartRequest(
    val code: String
)