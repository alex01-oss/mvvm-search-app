package com.loc.searchapp.data.repository

import com.loc.searchapp.data.network.CatalogApi
import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.CatalogResponse
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
    ): CatalogResponse {
        val response = api.getCatalog(
            searchQuery = searchQuery,
            searchType = searchType,
            page = page,
        )

        if (response.isSuccessful) {
            return response.body() ?: CatalogResponse()
        } else {
            throw Exception("API error: ${response.code()}")
        }
    }

    override suspend fun getCart(token: String): CartResponse {
        return api.getCart(token)
    }

    override suspend fun addProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun getProduct(code: String): Product? {
        return getProduct(code = code)
    }
}