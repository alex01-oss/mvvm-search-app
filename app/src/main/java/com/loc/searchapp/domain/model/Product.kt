package com.loc.searchapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    val code: String,
    val dimensions: String,
    val images: String,
    val shape: String,
    @SerialName("is_in_cart") val isInCart: Boolean
): Parcelable