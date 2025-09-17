package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.data.remote.dto.DetailsData
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.ItemCartRequest
import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(
        @Query("search_code") searchCode: String? = null,
        @Query("search_shape") searchShape: String? = null,
        @Query("search_dimensions") searchDimensions: String? = null,
        @Query("search_machine") searchMachine: String? = null,
        @Query("bond_ids") bondIds: List<Int>? = null,
        @Query("grid_size_ids") gridSizeIds: List<Int>? = null,
        @Query("mounting_ids") mountingIds: List<Int>? = null,
        @Query("category_id") categoryId: Int,
        @Query("page") page: Int,
        @Query("items_per_page") itemsPerPage: Int
    ): Response<CatalogDto>

    @GET("catalog/{id}")
    suspend fun getCatalogItem(
        @Path("id") id: Int
    ): Response<DetailsData>

    @GET("cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("cart/items")
    suspend fun addToCart(
        @Body addItemRequest: ItemCartRequest
    ): Response<ItemCartResponse>

    @DELETE("cart/items/{id}")
    suspend fun removeFromCart(
        @Path("id") id: Int
    ): Response<ItemCartResponse>

    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("filters")
    suspend fun getFilters(
        @Query("category_id") categoryId: Int,
    ): Response<FiltersResponse>
}