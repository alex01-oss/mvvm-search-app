package com.loc.searchapp.core.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartItem(
    val product: Product,
    val quantity: Int?,
): Parcelable
