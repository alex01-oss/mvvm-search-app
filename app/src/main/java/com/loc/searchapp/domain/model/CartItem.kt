package com.loc.searchapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val code: String,
    val shape: String,
    val dimensions: String,
    val quantity: Int?,
    val images: String,
): Parcelable
