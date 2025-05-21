package com.loc.searchapp.core.data.remote.dto

import com.loc.searchapp.core.domain.model.catalog.Product
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
data class CatalogItemDetailedResponse(
    val item: CatalogItemResponse,
    val bond: BondResponse?,
    val machines: List<MachineResponse>
)

@Serializable
data class CatalogItemResponse(
    val code: String,
    val shape: String?,
    val dimensions: String?,
    val images: String?,
    @SerialName("name_bond") val nameBond: String?,
    @SerialName("grid_size") val gridSize: String?,
    @SerialName("is_in_cart") val isInCart: Boolean
)

@Serializable
data class BondResponse(
    @SerialName("name_bond") val nameBond: String,
    @SerialName("bond_description") val bondDescription: String,
    @SerialName("bond_cooling") val bondCooling: String
)

@Serializable
data class MachineResponse(
    @SerialName("name_equipment") val nameEquipment: String,
    @SerialName("name_producer") val nameProducer: String
)