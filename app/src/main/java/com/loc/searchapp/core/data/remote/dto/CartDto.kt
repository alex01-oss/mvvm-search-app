package com.loc.searchapp.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartItem(
    val product: Product,
    val quantity: Int?,
): Parcelable
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
    @SerialName("product_id")
    val id: Int
)