package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.domain.repository.CatalogRepository
import javax.inject.Inject

class GetCart @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val userPreferences: UserPreferences,
) {
    suspend operator fun invoke(): List<CartItem> {
        val token = userPreferences.getToken() ?: throw Exception("Token not found")
        val response = catalogRepository.getCart("Bearer $token")
        return response.cart
    }
}