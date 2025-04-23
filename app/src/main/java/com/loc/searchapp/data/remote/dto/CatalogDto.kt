package com.loc.searchapp.data.remote.dto

import com.loc.searchapp.domain.model.Product
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
