package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.data.remote.dto.MenuResponse

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

    suspend fun getMenu(): MenuResponse
}