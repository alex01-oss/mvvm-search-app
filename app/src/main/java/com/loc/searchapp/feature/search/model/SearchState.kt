package com.loc.searchapp.feature.search.model

import androidx.paging.PagingData
import com.loc.searchapp.core.data.remote.dto.Product
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.SearchParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val page: Int = 1,
    val products: Flow<PagingData<Product>> = emptyFlow(),
    val token: String? = null,
    val categoryId: Int,

    val searchParams: SearchParams = SearchParams(),
    val filterParams: FilterParams = FilterParams()
)