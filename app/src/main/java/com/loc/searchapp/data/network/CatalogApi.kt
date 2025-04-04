package com.loc.searchapp.data.network

import com.loc.searchapp.data.network.dto.CartResponse
import com.loc.searchapp.data.network.dto.CatalogResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatalogApi {
    @GET("/api/woodworking")
    suspend fun getCatalog(
        @Query("search") searchQuery: String = "",
        @Query("search_type") searchType: String,
        @Query("page") page: Int = 1
    ): Response<CatalogResponse>

    @GET("/api/cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): CartResponse
}