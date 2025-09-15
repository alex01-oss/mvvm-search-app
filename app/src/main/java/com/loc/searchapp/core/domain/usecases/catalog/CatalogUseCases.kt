package com.loc.searchapp.core.domain.usecases.catalog

data class CatalogUseCases (
    val getCart: GetCart,
    val addProduct: AddProduct,
    val deleteProduct: DeleteProduct,
    val getCatalogPaging: GetCatalogPaging,
    val getCatalogItem: GetCatalogItem,
    val getFilters: GetFilters,
    val getCategories: GetCategories
)