package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class GetCart @Inject constructor(
    private val catalogRepository: CatalogRepository,
) {
    suspend operator fun invoke(): Cart {
        return catalogRepository.getCart()
    }
}