package com.loc.searchapp.data.repository

import com.loc.searchapp.data.remote.api.CatalogApi
import com.loc.searchapp.data.remote.dto.CartResponse
import com.loc.searchapp.data.remote.dto.CatalogDto
import com.loc.searchapp.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.data.remote.dto.ItemCartRequest
import com.loc.searchapp.data.remote.dto.ItemCartResponse
import com.loc.searchapp.data.remote.dto.MenuResponse
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val api: CatalogApi
): CatalogRepository {

    override suspend fun getCatalog(
        searchQuery: String,
        searchType: String,
        page: Int,
        token: String?
    ): CatalogDto {
        val response = api.getCatalog(
            searchQuery = searchQuery,
            searchType = searchType,
            page = page,
            token = token
        )

        if (response.isSuccessful) {
            return response.body() ?: CatalogDto()
        } else {
            throw Exception("API error: ${response.code()}")
        }
    }

    override suspend fun getCatalogItem(
        code: String
    ): CatalogItemDetailedResponse {
        return api.getCatalogItem(code)
    }

    override suspend fun getCart(
        token: String
    ): CartResponse {
        return api.getCart(token)
    }

    override suspend fun addProduct(
        code: String
    ): ItemCartResponse {
        return api.addToCart(
            ItemCartRequest(code)
        )
    }

    override suspend fun deleteProduct(
        code: String
    ): ItemCartResponse {
        return api.removeFromCart(
            ItemCartRequest(code)
        )
    }

    override suspend fun getProduct(
        code: String
    ): Product? {
        return getProduct(code = code)
    }

    override suspend fun getMenu(): MenuResponse {
        return api.getMenu()
    }
}