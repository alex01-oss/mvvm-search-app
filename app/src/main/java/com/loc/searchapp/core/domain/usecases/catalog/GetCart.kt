package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import retrofit2.Response
import javax.inject.Inject

class GetCart @Inject constructor(
    private val catalogRepository: CatalogRepository,
) {
    suspend operator fun invoke(
    ): Response<CartResponse> {
        return catalogRepository.getCart()
    }
}