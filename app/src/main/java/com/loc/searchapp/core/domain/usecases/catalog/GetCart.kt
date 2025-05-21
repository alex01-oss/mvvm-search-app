package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.domain.repository.CatalogRepository
import javax.inject.Inject

class GetCart @Inject constructor(
    private val catalogRepository: CatalogRepository,
) {
    suspend operator fun invoke(
        token: String
    ): List<CartItem> {
        val response = catalogRepository.getCart("Bearer $token")
        return response.cart
    }
}