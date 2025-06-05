package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.data.remote.dto.MenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(
        @Query("search") searchQuery: String = "",
        @Query("search_type") searchType: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String? = null
    ): Response<CatalogDto>

    @GET("catalog/{code}")
    suspend fun getCatalogItem(
        @Path("code") code: String
    ): CatalogItemDetailedResponse

    @GET("menu")
    suspend fun getMenu(): MenuResponse

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): CartResponse

    @POST("cart/items")
    suspend fun addToCart(
        @Body addItemRequest: ItemCartRequest
    ): ItemCartResponse

    @DELETE("cart/items/{code}")
    suspend fun removeFromCart(
        @Path("code") code: String
    ): ItemCartResponse
}