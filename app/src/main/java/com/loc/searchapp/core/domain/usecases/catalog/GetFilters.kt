package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import retrofit2.Response
import javax.inject.Inject

class GetFilters @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(
        categoryId: Int
    ): Response<FiltersResponse> {
        return catalogRepository.getFilters(categoryId)
    }
}