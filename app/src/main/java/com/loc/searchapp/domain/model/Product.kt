package com.loc.searchapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val code: String,
    val dimensions: String,
    val images: String,
    val shape: String,
    @SerializedName("is_in_cart") val isInCart: Boolean
): Parcelable