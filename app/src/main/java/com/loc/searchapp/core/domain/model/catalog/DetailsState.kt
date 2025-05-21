package com.loc.searchapp.core.domain.model.catalog

data class DetailsState(
    val product: Product? = null,
    val bond: Bond? = null,
    val machines: List<EquipmentModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)