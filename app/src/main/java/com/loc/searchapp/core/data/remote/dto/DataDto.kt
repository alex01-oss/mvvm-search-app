package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    @SerialName("img_url") val imgUrl: String
)

@Serializable
sealed class FilterItem {
    abstract val id: Int

    @Serializable
    data class Bond(
        override val id: Int,
        @SerialName("name_bond") val nameBond: String
    ) : FilterItem()

    @Serializable
    data class Grid(
        override val id: Int,
        @SerialName("grid_size") val gridSize: String
    ) : FilterItem()

    @Serializable
    data class Mounting(
        override val id: Int,
        val mm: String
    ) : FilterItem()
}

@Serializable
data class FiltersResponse(
    val bonds: List<FilterItem.Bond> = emptyList(),
    val grids: List<FilterItem.Grid> = emptyList(),
    val mountings: List<FilterItem.Mounting> = emptyList()
)
