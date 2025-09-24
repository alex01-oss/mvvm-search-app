package com.loc.searchapp.presentation.search.model

import androidx.paging.PagingData
import com.loc.searchapp.core.data.remote.dto.ProductDto
import com.loc.searchapp.core.domain.model.catalog.FilterParams
import com.loc.searchapp.core.domain.model.catalog.SearchParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val page: Int = 1,
    val products: Flow<PagingData<ProductDto>> = emptyFlow(),
    val token: String? = null,
    val categoryId: Int,

    val searchParams: SearchParams = SearchParams(),
    val filterParams: FilterParams = FilterParams()
)