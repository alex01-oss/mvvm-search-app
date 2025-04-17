package com.loc.searchapp.domain.repository

import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.Catalog
import com.loc.searchapp.data.network.dto.ItemCartResponse
import com.loc.searchapp.domain.model.Product

interface CatalogRepository {
    suspend fun getCatalog(
        searchQuery: String = "",
        searchType: String = "code",
        page: Int = 1,
        token: String?
    ): Catalog

    suspend fun getCart(
        token: String,
    ): CartResponse

    suspend fun addProduct(
        code: String
    ): ItemCartResponse

    suspend fun deleteProduct(
        code: String
    ): ItemCartResponse

    suspend fun getProduct(
        code: String
    ): Product?

    suspend fun getMenu(): Map<String, Any>
}