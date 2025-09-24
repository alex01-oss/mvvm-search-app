package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.MessageResult
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class DeleteProduct @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(data: CatalogId): MessageResult {
        return catalogRepository.deleteProduct(data)
    }
}