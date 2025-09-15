package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(
        @QueryMap filters: Map<String, @JvmSuppressWildcards Any>
    ): Response<CatalogDto>

    @GET("catalog/{id}")
    suspend fun getCatalogItem(
        @Path("id") id: Int
    ): CatalogItemDetailedResponse

    @GET("cart")
    suspend fun getCart(): CartResponse

    @POST("cart/items")
    suspend fun addToCart(
        @Body addItemRequest: ItemCartRequest
    ): ItemCartResponse

    @DELETE("cart/items/{id}")
    suspend fun removeFromCart(
        @Path("id") id: Int
    ): ItemCartResponse

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("filters")
    suspend fun getFilters(): FiltersResponse
}