package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.repository.CatalogRepository
import jakarta.inject.Inject

class GetFilters @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(data: CategoryId): Filters {
        return catalogRepository.getFilters(data)
    }
}