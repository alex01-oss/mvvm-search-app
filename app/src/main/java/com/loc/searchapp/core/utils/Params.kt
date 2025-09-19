package com.loc.searchapp.core.utils

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

data class PaginationParams(
    val page: Int = 1,
    val itemsPerPage: Int = 8
)
