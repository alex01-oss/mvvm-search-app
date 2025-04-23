package com.loc.searchapp.domain.usecases.catalog

data class CatalogUseCases (
    val getCart: GetCart,
    val addProduct: AddProduct,
    val deleteProduct: DeleteProduct,
    val selectProduct: SelectProduct,
    val getMenu: GetMenu,
    val getCatalogPaging: GetCatalogPaging
)