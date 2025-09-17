package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.DetailsData
import com.loc.searchapp.core.domain.repository.CatalogRepository
import retrofit2.Response

class GetCatalogItem(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(id: Int): Response<DetailsData> {
        return repository.getCatalogItem(id)
    }
}