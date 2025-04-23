package com.loc.searchapp.data.remote.dto

import com.loc.searchapp.domain.model.CartItem
import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val cart: List<CartItem>
)

@Serializable
data class ItemCartResponse(
    val message: String
)

@Serializable
data class ItemCartRequest(
    val code: String
)