package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.domain.repository.CatalogRepository
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