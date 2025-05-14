package com.loc.searchapp.domain.repository

import com.loc.searchapp.data.remote.dto.CartResponse
import com.loc.searchapp.data.remote.dto.CatalogDto
import com.loc.searchapp.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.data.remote.dto.ItemCartResponse
import com.loc.searchapp.data.remote.dto.MenuResponse
import com.loc.searchapp.domain.model.Product

interface CatalogRepository {
    suspend fun getCatalog(
        searchQuery: String = "",
        searchType: String = "code",
        page: Int = 1,
        token: String?
    ): CatalogDto

    suspend fun getCatalogItem(
        code: String
    ): CatalogItemDetailedResponse

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

    suspend fun getMenu(): MenuResponse
}