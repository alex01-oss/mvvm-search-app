package com.loc.searchapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    val code: String,
    val shape: String,
    val dimensions: String,
    val images: String,
    @SerialName("name_bond") val nameBond: String,
    @SerialName("grid_size") val gridSize: String,
    @SerialName("is_in_cart") val isInCart: Boolean
): Parcelable