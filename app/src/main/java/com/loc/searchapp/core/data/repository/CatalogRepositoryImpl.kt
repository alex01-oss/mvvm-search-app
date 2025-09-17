package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.remote.api.CatalogApi
import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.DetailsData
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.PaginationParams
import com.loc.searchapp.core.utils.SearchParams
import retrofit2.Response
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val api: CatalogApi
): CatalogRepository {

    override suspend fun getCatalog(
        search: SearchParams,
        filters: FilterParams,
        pagination: PaginationParams,
        categoryId: Int
    ): Response<CatalogDto> {
        return api.getCatalog(
            searchCode = search.searchCode.takeIf { it.isNotBlank() },
            searchShape = search.searchShape.takeIf { it.isNotBlank() },
            searchDimensions = search.searchDimensions.takeIf { it.isNotBlank() },
            searchMachine = search.searchMachine.takeIf { it.isNotBlank() },
            bondIds = filters.bondIds.takeIf { it.isNotEmpty() },
            gridSizeIds = filters.gridSizeIds.takeIf { it.isNotEmpty() },
            mountingIds = filters.mountingIds.takeIf { it.isNotEmpty() },
            categoryId = categoryId,
            page = pagination.page,
            itemsPerPage = pagination.itemsPerPage
        )
    }

    override suspend fun getCatalogItem(
        id: Int
    ): Response<DetailsData> {
        return api.getCatalogItem(id)
    }

    override suspend fun getCart(): Response<CartResponse> {
        return api.getCart()
    }

    override suspend fun addProduct(
        id: Int
    ): Response<ItemCartResponse> {
        return api.addToCart(
            ItemCartRequest(id)
        )
    }

    override suspend fun deleteProduct(
        id: Int
    ): Response<ItemCartResponse> {
        return api.removeFromCart(id)
    }

    override suspend fun getCategories(): Response<List<Category>> {
        return api.getCategories()
    }

    override suspend fun getFilters(
        categoryId: Int
    ): Response<FiltersResponse> {
        return api.getFilters(categoryId)
    }
}