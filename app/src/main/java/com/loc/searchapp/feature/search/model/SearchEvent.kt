package com.loc.searchapp.feature.search.model

sealed class SearchEvent {
    data class UpdateSearchQuery(
        val searchQuery: String,
        val searchType: String,
        val page: Int
    ): SearchEvent()

    object SearchProducts: SearchEvent()

    data class ChangeSearchType(val searchType: String): SearchEvent()
}