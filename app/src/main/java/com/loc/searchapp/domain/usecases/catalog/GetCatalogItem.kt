package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.data.remote.dto.CatalogItemDetailedResponse
import com.loc.searchapp.domain.repository.CatalogRepository

class GetCatalogItem(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        code: String
    ): CatalogItemDetailedResponse {
        return repository.getCatalogItem(code)
    }
}