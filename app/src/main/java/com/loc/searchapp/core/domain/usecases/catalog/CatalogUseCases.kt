package com.loc.searchapp.core.domain.usecases.catalog

import jakarta.inject.Inject

data class CatalogUseCases @Inject constructor(
    val getCart: GetCart,
    val addProduct: AddProduct,
    val deleteProduct: DeleteProduct,
    val getCatalogPaging: GetCatalogPaging,
    val getCatalogItem: GetCatalogItem,
    val getFilters: GetFilters,
    val getCategories: GetCategories
)