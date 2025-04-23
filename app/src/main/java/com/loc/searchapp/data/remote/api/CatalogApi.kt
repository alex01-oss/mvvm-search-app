package com.loc.searchapp.data.remote.api

import com.loc.searchapp.data.remote.dto.CartResponse
import com.loc.searchapp.data.remote.dto.CatalogDto
import com.loc.searchapp.data.remote.dto.ItemCartRequest
import com.loc.searchapp.data.remote.dto.ItemCartResponse
import com.loc.searchapp.data.remote.dto.MenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CatalogApi {
    @GET("/api/catalog")
    suspend fun getCatalog(
        @Query("search") searchQuery: String = "",
        @Query("search_type") searchType: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String? = null
    ): Response<CatalogDto>

    @GET("api/menu")
    suspend fun getMenu(): MenuResponse

    @GET("/api/cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): CartResponse

    @POST("api/cart")
    suspend fun addToCart(
        @Body addItemRequest: ItemCartRequest
    ): ItemCartResponse

    @HTTP(method = "DELETE", path = "api/cart", hasBody = true)
    suspend fun removeFromCart(
        @Body removeItemRequest: ItemCartRequest
    ): ItemCartResponse
}