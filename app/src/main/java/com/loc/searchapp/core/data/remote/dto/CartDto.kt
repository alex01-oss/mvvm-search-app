package com.loc.searchapp.core.data.remote.dto

import com.loc.searchapp.core.domain.model.catalog.CartItem
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
    val id: Int
)