package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.domain.model.catalog.Catalog
import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.CatalogParams
import com.loc.searchapp.core.domain.model.catalog.Category
import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.model.catalog.MessageResult
import com.loc.searchapp.core.domain.model.catalog.ProductDetails

interface CatalogRepository {
    suspend fun getCatalog(params: CatalogParams, page: Int): Catalog
    suspend fun getCatalogItem(data: CatalogId): ProductDetails
    suspend fun getCart(): Cart
    suspend fun addProduct(data: CatalogId): MessageResult
    suspend fun deleteProduct(data: CatalogId): MessageResult
    suspend fun getCategories(): List<Category>
    suspend fun getFilters(data: CategoryId): Filters
}