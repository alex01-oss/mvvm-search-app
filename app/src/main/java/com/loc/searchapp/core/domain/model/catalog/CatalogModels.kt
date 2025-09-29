package com.loc.searchapp.core.domain.model.catalog

data class CategoryId(
    val categoryId: Int
)

data class CatalogId(
    val id: Int
)

data class Limit(
    val limit: Int
)

sealed class FilterItem {
    abstract val id: Int

    data class Bond(
        override val id: Int,
        val name: String
    ) : FilterItem()

    data class Grid(
        override val id: Int,
        val size: String
    ) : FilterItem()

    data class Mounting(
        override val id: Int,
        val mm: String
    ) : FilterItem()
}

data class Filters(
    val bonds: List<FilterItem.Bond>,
    val grids: List<FilterItem.Grid>,
    val mountings: List<FilterItem.Mounting>,
)

data class Category(
    val id: Int,
    val name: String,
    val imgUrl: String
)

data class MessageResult(
    val message: String
)

data class DetailsMounting(
    val mm: String
)

data class Product(
    val id: Int,
    val code: String,
    val shape: String,
    val dimensions: String,
    val images: String,
    val nameBonds: List<String>,
    val gridSize: String,
    val mounting: DetailsMounting?,
    val isInCart: Boolean
)

data class CartItem(
    val product: Product,
    val quantity: Int?,
)

data class Cart(
    val cart: List<CartItem>
)

data class Catalog(
    val items: List<Product>,
    val currentPage: Int,
    val itemsPerPage: Int,
    val totalItems: Int,
    val totalPages: Int
)

data class DetailsBond(
    val nameBond: String,
    val bondDescription: String,
    val bondCooling: String
)

data class EquipmentModel(
    val model: String,
    val name: String,
)

data class ProductDetails(
    val item: Product,
    val bonds: List<DetailsBond>,
    val machines: List<EquipmentModel>,
    val mounting: DetailsMounting?
)

data class CatalogParams(
    val searchCode: String? = null,
    val searchShape: String? = null,
    val searchDimensions: String? = null,
    val searchMachine: String? = null,
    val bondIds: List<Int>? = null,
    val gridSizeIds: List<Int>? = null,
    val mountingIds: List<Int>? = null,
    val categoryId: Int,
    val itemsPerPage: Int,
)
