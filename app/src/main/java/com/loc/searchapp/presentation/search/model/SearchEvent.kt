package com.loc.searchapp.presentation.search.model

import com.loc.searchapp.core.domain.model.catalog.FilterParams
import com.loc.searchapp.core.domain.model.catalog.SearchParams

sealed class SearchEvent {
    data class UpdateSearchQuery(
        val searchParams: SearchParams = SearchParams(),
        val filterParams: FilterParams = FilterParams(),

        val categoryId: Int,
        val page: Int
    ): SearchEvent()

    object SearchProducts: SearchEvent()
}