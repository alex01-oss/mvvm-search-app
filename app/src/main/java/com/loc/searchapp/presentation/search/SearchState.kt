package com.loc.searchapp.presentation.search

import com.loc.searchapp.domain.model.ListItem

data class SearchState(
    val searchQuery: String = "",
    val searchType: String = "code",
    val page: Int = 1,
    val products: List<ListItem> = emptyList()
)