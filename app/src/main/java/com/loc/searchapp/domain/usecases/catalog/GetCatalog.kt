package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.repository.CatalogRepository
import javax.inject.Inject

class GetCatalog @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(
        searchQuery: String = "",
        searchType: String = "code",
        page: Int = 1,
    ): List<Product> {
        val response = catalogRepository.getCatalog(
            searchQuery = searchQuery,
            searchType = searchType,
            page = page,
        )
        return response.items
    }
}