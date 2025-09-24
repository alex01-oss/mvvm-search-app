package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.Category
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class GetCategories @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): List<Category> {
        return catalogRepository.getCategories()
    }
}