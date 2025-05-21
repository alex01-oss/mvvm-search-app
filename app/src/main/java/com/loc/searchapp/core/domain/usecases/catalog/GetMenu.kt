package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.MenuResponse
import com.loc.searchapp.core.domain.repository.CatalogRepository
import javax.inject.Inject

class GetMenu @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): MenuResponse {
        return catalogRepository.getMenu()
    }
}