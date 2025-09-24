package com.loc.searchapp.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartItemDto(
    val product: ProductDto,
    val quantity: Int?,
): Parcelable
@Serializable
data class CartResponse(
    val cart: List<CartItemDto>
)

@Serializable
data class MessageResponse(
    val message: String
)

@Serializable
data class ItemCartRequest(
    @SerialName("product_id")
    val id: Int
)