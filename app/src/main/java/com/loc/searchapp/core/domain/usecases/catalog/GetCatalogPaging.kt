package com.loc.searchapp.core.domain.usecases.catalog

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.repository.CatalogRepository
import com.loc.searchapp.core.utils.CatalogPagingSource
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.core.utils.SearchParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatalogPaging @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    operator fun invoke(
        search: SearchParams = SearchParams(),
        filters: FilterParams = FilterParams(),
        categoryId: Int = 1,
        itemsPerPage: Int = 8,
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CatalogPagingSource(
                    repository = catalogRepository,
                    search = search,
                    filters = filters,
                    categoryId = categoryId,
                    itemsPerPage = itemsPerPage,
                )
            }
        ).flow
    }
}
