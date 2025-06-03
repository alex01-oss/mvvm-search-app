package com.loc.searchapp.core.domain.model.catalog

data class DetailsData(
    val product: Product,
    val bond: Bond? = null,
    val machines: List<EquipmentModel> = emptyList(),
)