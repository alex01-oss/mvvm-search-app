package com.loc.searchapp.domain.usecases.catalog

import com.loc.searchapp.data.network.dto.MenuResponse
import com.loc.searchapp.domain.repository.CatalogRepository
import javax.inject.Inject

class GetMenu @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): MenuResponse {
        return catalogRepository.getMenu()
    }
}