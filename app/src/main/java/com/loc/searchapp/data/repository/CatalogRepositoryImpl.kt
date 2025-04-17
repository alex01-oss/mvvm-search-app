package com.loc.searchapp.data.repository

import com.loc.searchapp.data.network.CatalogApi
import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.Catalog
import com.loc.searchapp.data.network.dto.ItemCartRequest
import com.loc.searchapp.data.network.dto.ItemCartResponse
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
    ): Catalog {
        val response = api.getCatalog(
            searchQuery = searchQuery,
            searchType = searchType,
            page = page,
            token = token
        )

        if (response.isSuccessful) {
            return response.body() ?: Catalog()
        } else {
            throw Exception("API error: ${response.code()}")
        }
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

    override suspend fun getMenu(): Map<String, Any> {
        return api.getMenu()
    }
}