package com.loc.searchapp.core.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    val id: Int,
    val code: String,
    val shape: String,
    val dimensions: String,
    val images: String,
    @SerialName("name_bonds") val nameBonds: List<String>,
    @SerialName("grid_size") val gridSize: String,
    val mounting: MountingDetail?,
    @SerialName("is_in_cart") val isInCart: Boolean
): Parcelable

@Parcelize
@Serializable
data class MountingDetail(
    val mm: String
): Parcelable