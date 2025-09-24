package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams

interface AutocompleteRepository{
    suspend fun autocompleteCode(params: AutocompleteParams): List<String>
    suspend fun autocompleteShape(params: AutocompleteParams): List<String>
    suspend fun autocompleteDimensions(params: AutocompleteParams): List<String>
    suspend fun autocompleteMachine(params: AutocompleteParams): List<String>
}