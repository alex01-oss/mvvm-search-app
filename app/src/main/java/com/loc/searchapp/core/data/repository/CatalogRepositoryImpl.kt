package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.remote.api.CatalogApi
import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.PaginationParams
import com.loc.searchapp.core.utils.SearchParams
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val api: CatalogApi
): CatalogRepository {

    override suspend fun getCatalog(
        search: SearchParams,
        filters: FilterParams,
        pagination: PaginationParams,
        categoryId: Int
    ): CatalogDto {
        val queryMap = mutableMapOf<String, Any>(
            "search_code" to search.searchCode,
            "search_shape" to search.searchShape,
            "search_dimensions" to search.searchDimensions,
            "search_machine" to search.searchMachine,
            "category_id" to categoryId,
            "page" to pagination.page,
            "items_per_page" to pagination.itemsPerPage
        )

        filters.bondIds.forEach { id -> queryMap.put("bond_ids", id) }
        filters.gridSizeIds.forEach { id -> queryMap.put("grid_size_ids", id) }
        filters.mountingIds.forEach { id -> queryMap.put("mounting_ids", id) }

        val response = api.getCatalog(queryMap)

        if (response.isSuccessful) {
            return response.body() ?: CatalogDto()
        } else {
            throw Exception("API error: ${response.code()}")
        }
    }


    override suspend fun getCatalogItem(
        id: Int
    ): CatalogItemDetailedResponse {
        return api.getCatalogItem(id)
    }

    override suspend fun getCart(): CartResponse {
        return api.getCart()
    }

    override suspend fun addProduct(
        id: Int
    ): ItemCartResponse {
        return api.addToCart(
            ItemCartRequest(id)
        )
    }

    override suspend fun deleteProduct(
        id: Int
    ): ItemCartResponse {
        return api.removeFromCart(id)
    }

    override suspend fun getCategories(): List<Category> {
        return api.getCategories()
    }

    override suspend fun getFilters(): FiltersResponse {
        return api.getFilters()
    }
}