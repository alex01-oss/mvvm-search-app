package com.loc.searchapp.presentation.shared.model

sealed class ProductSideEffect {
    data class ShowSnackbar(val message: String) : ProductSideEffect()
}
