package com.loc.searchapp.core.domain.usecases.autocomplete

import jakarta.inject.Inject

data class AutocompleteUseCases @Inject constructor(
    val autocompleteCode: AutocompleteCode,
    val autocompleteShape: AutocompleteShape,
    val autocompleteDimensions: AutocompleteDimensions,
    val autocompleteMachine: AutocompleteMachine
)
