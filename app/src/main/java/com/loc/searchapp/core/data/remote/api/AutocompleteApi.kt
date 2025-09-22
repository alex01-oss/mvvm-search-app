package com.loc.searchapp.core.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AutocompleteApi {
    @GET("autocomplete/code")
    suspend fun autocompleteCode(
        @QueryMap params: Map<String, String>
    ): Response<List<String>>

    @GET("autocomplete/shape")
    suspend fun autocompleteShape(
        @QueryMap params: Map<String, String>
    ): Response<List<String>>

    @GET("autocomplete/dimensions")
    suspend fun autocompleteDimensions(
        @QueryMap params: Map<String, String>
    ): Response<List<String>>

    @GET("autocomplete/machine")
    suspend fun autocompleteMachine(
        @QueryMap params: Map<String, String>
    ): Response<List<String>>
}
