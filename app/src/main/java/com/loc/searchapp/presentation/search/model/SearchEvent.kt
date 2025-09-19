package com.loc.searchapp.presentation.search.model

import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.SearchParams

sealed class SearchEvent {
    data class UpdateSearchQuery(
        val searchParams: SearchParams = SearchParams(),
        val filterParams: FilterParams = FilterParams(),

        val categoryId: Int,
        val page: Int
    ): SearchEvent()

    object SearchProducts: SearchEvent()
}