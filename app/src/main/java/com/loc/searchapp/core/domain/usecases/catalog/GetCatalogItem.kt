package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.ProductDetails
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class GetCatalogItem @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        data: CatalogId
    ): ProductDetails {
        return repository.getCatalogItem(data)
    }
}