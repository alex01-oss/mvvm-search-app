package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    @SerialName("img_url") val imgUrl: String
)

@Serializable
sealed class FilterItemDto {
    abstract val id: Int

    @Serializable
    data class BondDto(
        override val id: Int,
        @SerialName("name_bond") val nameBond: String
    ) : FilterItemDto()

    @Serializable
    data class GridDto(
        override val id: Int,
        @SerialName("grid_size") val gridSize: String
    ) : FilterItemDto()

    @Serializable
    data class MountingDto(
        override val id: Int,
        val mm: String
    ) : FilterItemDto()
}

@Serializable
data class FiltersResponse(
    val bonds: List<FilterItemDto.BondDto> = emptyList(),
    val grids: List<FilterItemDto.GridDto> = emptyList(),
    val mountings: List<FilterItemDto.MountingDto> = emptyList()
)
