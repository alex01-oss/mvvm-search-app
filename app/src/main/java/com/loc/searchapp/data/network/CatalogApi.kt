package com.loc.searchapp.data.network

import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.Catalog
import com.loc.searchapp.data.network.dto.ItemCartRequest
import com.loc.searchapp.data.network.dto.ItemCartResponse
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
    ): Response<Catalog>

    @GET("api/menu")
    suspend fun getMenu(): Map<String, Any>

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