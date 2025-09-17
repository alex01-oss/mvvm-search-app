package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.ItemCartResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import retrofit2.Response
import javax.inject.Inject

class AddProduct @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(
        id: Int
    ): Response<ItemCartResponse> {
        return catalogRepository.addProduct(id)
    }
}