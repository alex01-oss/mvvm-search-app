package com.loc.searchapp.presentation.details

import com.loc.searchapp.domain.model.Product

sealed class DetailsEvent {

    data class UpsertDeleteProduct(val product: Product): DetailsEvent()

    object RemoveSideEffect: DetailsEvent()
}
