package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.domain.repository.CatalogRepository
import javax.inject.Inject

class GetCategories @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(): List<Category> {
        val res = catalogRepository.getCategories()
        return res
    }
}