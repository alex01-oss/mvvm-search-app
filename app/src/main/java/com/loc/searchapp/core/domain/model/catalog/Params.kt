package com.loc.searchapp.core.domain.model.catalog

data class SearchParams(
    val searchCode: String = "",
    val searchShape: String = "",
    val searchDimensions: String = "",
    val searchMachine: String = "",
) {
    fun hasAnyValue(): Boolean =
        listOf(searchCode, searchShape, searchDimensions, searchMachine).any { it.isNotBlank() }
}

data class FilterParams(
    val bondIds: List<Int> = emptyList(),
    val gridSizeIds: List<Int> = emptyList(),
    val mountingIds: List<Int> = emptyList(),
)

fun SearchParams.toCatalogParams(
    filterParams: FilterParams,
    categoryId: Int,
    itemsPerPage: Int = 8
) = CatalogParams(
    searchCode = searchCode.takeIf { it.isNotBlank() },
    searchShape = searchShape.takeIf { it.isNotBlank() },
    searchDimensions = searchDimensions.takeIf { it.isNotBlank() },
    searchMachine = searchMachine.takeIf { it.isNotBlank() },
    bondIds = filterParams.bondIds.takeIf { it.isNotEmpty() },
    gridSizeIds = filterParams.gridSizeIds.takeIf { it.isNotEmpty() },
    mountingIds = filterParams.mountingIds.takeIf { it.isNotEmpty() },
    categoryId = categoryId,
    itemsPerPage = itemsPerPage,
)