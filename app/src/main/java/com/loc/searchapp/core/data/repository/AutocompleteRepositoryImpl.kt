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
        val res = api.autocompleteCode(params.toQueryMap())
        if (res.isSuccessful) {
            return res.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${res.code()}")
        }
    }

    override suspend fun autocompleteShape(params: AutocompleteParams): List<String> {
        val res = api.autocompleteShape(params.toQueryMap())
        if (res.isSuccessful) {
            return res.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${res.code()}")
        }
    }

    override suspend fun autocompleteDimensions(params: AutocompleteParams): List<String> {
        val res = api.autocompleteDimensions(params.toQueryMap())
        if (res.isSuccessful) {
            return res.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${res.code()}")
        }
    }

    override suspend fun autocompleteMachine(params: AutocompleteParams): List<String> {
        val res = api.autocompleteMachine(params.toQueryMap())
        if (res.isSuccessful) {
            return res.body() ?: emptyList()
        } else {
            throw RuntimeException("Autocomplete API error: ${res.code()}")
        }
    }
}