package com.loc.searchapp.core.domain.model.autocomplete

data class AutocompleteParams(
    val query: String,
    val categoryId: Int? = null,
    val searchCode: String? = null,
    val searchShape: String? = null,
    val searchDimensions: String? = null,
    val searchMachine: String? = null,
    val bondIds: List<Int> = emptyList(),
    val gridSizeIds: List<Int> = emptyList(),
    val mountingIds: List<Int> = emptyList()
)