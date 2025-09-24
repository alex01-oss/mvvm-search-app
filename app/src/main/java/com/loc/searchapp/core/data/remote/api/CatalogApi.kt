package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CategoryDto
import com.loc.searchapp.core.data.remote.dto.DetailedProductResponse
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(
        @QueryMap params: Map<String, String>
    ): Response<CatalogDto>

    @GET("catalog/{id}")
    suspend fun getCatalogItem(
        @Path("id") id: Int
    ): Response<DetailedProductResponse>

    @GET("cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("cart/items")
    suspend fun addToCart(
        @Body addItemRequest: ItemCartRequest
    ): Response<MessageResponse>

    @DELETE("cart/items/{id}")
    suspend fun removeFromCart(
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("filters")
    suspend fun getFilters(
        @Query("category_id") categoryId: Int,
    ): Response<FiltersResponse>
}