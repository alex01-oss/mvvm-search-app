package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository

class GetCatalogItem(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        id: Int
    ): CatalogItemDetailedResponse {
        return repository.getCatalogItem(id)
    }
}