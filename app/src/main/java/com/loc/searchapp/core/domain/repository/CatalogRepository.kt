package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.DetailsData
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.PaginationParams
import com.loc.searchapp.core.utils.SearchParams
import retrofit2.Response

interface CatalogRepository {
    suspend fun getCatalog(
        search: SearchParams = SearchParams(),
        filters: FilterParams = FilterParams(),
        pagination: PaginationParams = PaginationParams(),
        categoryId: Int,
    ): Response<CatalogDto>

    suspend fun getCatalogItem(
        id: Int
    ): Response<DetailsData>

    suspend fun getCart(): Response<CartResponse>

    suspend fun addProduct(
        id: Int
    ): Response<ItemCartResponse>

    suspend fun deleteProduct(
        id: Int
    ): Response<ItemCartResponse>

    suspend fun getCategories(): Response<List<Category>>

    suspend fun getFilters(
        categoryId: Int
    ): Response<FiltersResponse>
}