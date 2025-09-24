package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.mappers.toDomain
import com.loc.searchapp.core.data.mappers.toDto
import com.loc.searchapp.core.data.mappers.toQueryMap
import com.loc.searchapp.core.data.mappers.toQueryParam
import com.loc.searchapp.core.data.remote.api.CatalogApi
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.domain.model.catalog.Catalog
import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.CatalogParams
import com.loc.searchapp.core.domain.model.catalog.Category
import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.model.catalog.MessageResult
import com.loc.searchapp.core.domain.model.catalog.ProductDetails
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val api: CatalogApi
) : CatalogRepository {

    override suspend fun getCatalog(params: CatalogParams, page: Int): Catalog {
        val res = api.getCatalog(params.toQueryMap(page))
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Catalog fetch successful but body is null")
        } else {
            throw RuntimeException("Catalog fetch failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getCatalogItem(data: CatalogId): ProductDetails {
        val res = api.getCatalogItem(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Catalog item fetch successful but body is null")
        } else {
            throw RuntimeException("Catalog item fetch failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getCart(): Cart {
        val res = api.getCart()
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Get cart successful but body is null")
        } else {
            throw RuntimeException("Get cart failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun addProduct(data: CatalogId): MessageResult {
        val res = api.addToCart(ItemCartRequest(data.toDto()))
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Add product successful but body is null")
        } else {
            throw RuntimeException("Add product failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun deleteProduct(data: CatalogId): MessageResult {
        val res = api.removeFromCart(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Delete product successful but body is null")
        } else {
            throw RuntimeException("Delete product failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getCategories(): List<Category> {
        val res = api.getCategories()
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Get categories successful but body is null")
        } else {
            throw RuntimeException("Get categories failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getFilters(data: CategoryId): Filters {
        val res = api.getFilters(data.toQueryParam())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Get filters successful but body is null")
        } else {
            throw RuntimeException("Get filters failed: ${res.code()} ${res.message()}")
        }
    }
}