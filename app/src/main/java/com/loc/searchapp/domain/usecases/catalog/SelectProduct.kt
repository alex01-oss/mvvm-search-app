package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.repository.CatalogRepository

class SelectProduct (
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(code: String): Product? {
        return catalogRepository.getProduct(code = code)
    }
}