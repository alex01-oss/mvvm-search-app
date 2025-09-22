package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.mappers.toQueryMap
import com.loc.searchapp.core.data.remote.api.AutocompleteApi
import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams
import com.loc.searchapp.core.domain.repository.AutocompleteRepository
import jakarta.inject.Inject

class AutocompleteRepositoryImpl @Inject constructor(
    private val api: AutocompleteApi
): AutocompleteRepository {
    override suspend fun autocompleteCode(params: AutocompleteParams): List<String> {
        val response = api.autocompleteCode(params.toQueryMap())
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${response.code()}")
        }
    }

    override suspend fun autocompleteShape(params: AutocompleteParams): List<String> {
        val response = api.autocompleteShape(params.toQueryMap())
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${response.code()}")
        }
    }

    override suspend fun autocompleteDimensions(params: AutocompleteParams): List<String> {
        val response = api.autocompleteDimensions(params.toQueryMap())
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${response.code()}")
        }
    }

    override suspend fun autocompleteMachine(params: AutocompleteParams): List<String> {
        val response = api.autocompleteMachine(params.toQueryMap())
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${response.code()}")
        }
    }
}