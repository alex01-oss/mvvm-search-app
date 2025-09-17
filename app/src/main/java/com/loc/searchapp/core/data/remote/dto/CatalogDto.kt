package com.loc.searchapp.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogDto(
    val items: List<Product> = emptyList(),
    @SerialName("current_page") val currentPage: Int = 1,
    @SerialName("items_per_page") val itemsPerPage: Int = 8,
    @SerialName("total_items") val totalItems: Int = 0,
    @SerialName("total_pages") val totalPages: Int = 1
)

@Serializable
data class DetailsData(
    val item: Product,
    val bonds: List<Bond>,
    val machines: List<EquipmentModel> = emptyList(),
    val mounting: Mounting? = null
)

@Parcelize
@Serializable
data class Product(
    val id: Int,
    val code: String,
    val shape: String,
    val dimensions: String,
    val images: String,
    @SerialName("name_bonds") val nameBonds: List<String> = emptyList(),
    @SerialName("grid_size") val gridSize: String = "",
    val mounting: Mounting? = null,
    @SerialName("is_in_cart") val isInCart: Boolean = false
): Parcelable

@Parcelize
@Serializable
data class Mounting(
    val mm: String
): Parcelable

@Serializable
@Parcelize
data class Bond(
    @SerialName("name_bond") val nameBond: String,
    @SerialName("bond_description") val bondDescription: String,
    @SerialName("bond_cooling") val bondCooling: String
): Parcelable

@Parcelize
@Serializable
data class EquipmentModel(
    val model: String,
    @SerialName("name_producer") val name: String,
): Parcelable