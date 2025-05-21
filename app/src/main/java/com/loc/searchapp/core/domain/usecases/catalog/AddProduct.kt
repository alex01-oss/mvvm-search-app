package com.loc.searchapp.core.domain.usecases.catalog

import com.loc.searchapp.core.domain.repository.CatalogRepository
import javax.inject.Inject

class AddProduct @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(code: String) {
        catalogRepository.addProduct(code = code)
    }
}