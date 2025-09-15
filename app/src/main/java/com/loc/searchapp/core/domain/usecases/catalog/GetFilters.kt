package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import javax.inject.Inject

class GetFilters @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): FiltersResponse {
        val res = catalogRepository.getFilters()
        return res
    }
}