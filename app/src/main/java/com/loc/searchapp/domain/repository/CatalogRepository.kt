package com.loc.searchapp.domain.repository

import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.CatalogResponse
import com.loc.searchapp.domain.model.Product

interface CatalogRepository {
    suspend fun getCatalog(
        searchQuery: String = "",
        searchType: String = "code",
        page: Int = 1,
    ): CatalogResponse

    suspend fun getCart(
        token: String,
    ): CartResponse

    suspend fun addProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getProduct(code: String): Product?
}