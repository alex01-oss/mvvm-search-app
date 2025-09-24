package com.loc.searchapp.core.domain.usecases.catalog

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.searchapp.core.domain.model.catalog.CatalogParams
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.repository.CatalogRepository
import com.loc.searchapp.core.utils.CatalogPagingSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCatalogPaging @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    operator fun invoke(
        params: CatalogParams
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CatalogPagingSource(
                    repository = catalogRepository,
                    catalogParams = params
                )
            }
        ).flow
    }
}
