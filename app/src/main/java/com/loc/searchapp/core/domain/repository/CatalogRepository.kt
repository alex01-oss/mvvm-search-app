package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.PaginationParams
import com.loc.searchapp.core.utils.SearchParams

interface CatalogRepository {
    suspend fun getCatalog(
        search: SearchParams = SearchParams(),
        filters: FilterParams = FilterParams(),
        pagination: PaginationParams = PaginationParams(),
        categoryId: Int = 1,
    ): CatalogDto

    suspend fun getCatalogItem(
        id: Int
    ): CatalogItemDetailedResponse

    suspend fun getCart(): CartResponse

    suspend fun addProduct(
        id: Int
    ): ItemCartResponse

    suspend fun deleteProduct(
        id: Int
    ): ItemCartResponse

    suspend fun getCategories(): List<Category>

    suspend fun getFilters(): FiltersResponse
}