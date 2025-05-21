package com.loc.searchapp.feature.search.model

import androidx.paging.PagingData
import com.loc.searchapp.core.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val searchQuery: String = "",
    val searchType: String = "code",
    val page: Int = 1,
    val products: Flow<PagingData<Product>> = emptyFlow(),
    val token: String? = null,
    val placeholder: String = "Enter code"
)