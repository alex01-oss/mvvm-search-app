package com.loc.searchapp.core.domain.usecases.autocomplete

import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams
import com.loc.searchapp.core.domain.repository.AutocompleteRepository
import jakarta.inject.Inject

class AutocompleteCode @Inject constructor(
    private val autocompleteRepository: AutocompleteRepository
) {
    suspend operator fun invoke(
        params: AutocompleteParams
    ): List<String> {
        return autocompleteRepository.autocompleteCode(params)
    }
}