package com.loc.searchapp.core.data.mappers

import com.loc.searchapp.core.data.remote.dto.CartItemDto
import com.loc.searchapp.core.data.remote.dto.CartResponse
import com.loc.searchapp.core.data.remote.dto.CatalogDto
import com.loc.searchapp.core.data.remote.dto.CategoryDto
import com.loc.searchapp.core.data.remote.dto.DetailedProductResponse
import com.loc.searchapp.core.data.remote.dto.DetailsBondDto
import com.loc.searchapp.core.data.remote.dto.DetailsMountingDto
import com.loc.searchapp.core.data.remote.dto.EquipmentModelDto
import com.loc.searchapp.core.data.remote.dto.FilterItemDto
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.data.remote.dto.MessageResponse
import com.loc.searchapp.core.data.remote.dto.ProductDto
import com.loc.searchapp.core.domain.model.catalog.Cart
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.domain.model.catalog.Catalog
import com.loc.searchapp.core.domain.model.catalog.CatalogId
import com.loc.searchapp.core.domain.model.catalog.CatalogParams
import com.loc.searchapp.core.domain.model.catalog.Category
import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.DetailsBond
import com.loc.searchapp.core.domain.model.catalog.DetailsMounting
import com.loc.searchapp.core.domain.model.catalog.EquipmentModel
import com.loc.searchapp.core.domain.model.catalog.FilterItem
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.model.catalog.MessageResult
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.model.catalog.ProductDetails

fun CategoryId.toQueryParam(): Int = this.categoryId

fun FiltersResponse.toDomain(): Filters {
    return Filters(
        bonds = bonds.map { it.toDomain() },
        grids = grids.map { it.toDomain() },
        mountings = mountings.map { it.toDomain() },
    )
}

fun FilterItemDto.BondDto.toDomain(): FilterItem.Bond {
    return FilterItem.Bond(
        id = this.id,
        name = this.nameBond
    )
}

fun FilterItemDto.GridDto.toDomain(): FilterItem.Grid {
    return FilterItem.Grid(
        id = this.id,
        size = this.gridSize
    )
}

fun FilterItemDto.MountingDto.toDomain(): FilterItem.Mounting {
    return FilterItem.Mounting(
        id = this.id,
        mm = this.mm
    )
}

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        imgUrl = this.imgUrl
    )
}

fun List<CategoryDto>.toDomain(): List<Category> {
    return this.map { it.toDomain() }
}

fun CatalogId.toDto(): Int = this.id

fun MessageResponse.toDomain(): MessageResult {
    return MessageResult(
        message = this.message
    )
}

fun DetailsMountingDto.toDomain(): DetailsMounting {
    return DetailsMounting(
        mm = this.mm
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id,
        code = this.code,
        shape = this.shape,
        dimensions = this.dimensions,
        images = this.images,
        nameBonds = this.nameBonds,
        gridSize = this.gridSize,
        mounting = this.mounting?.toDomain(),
        isInCart = this.isInCart
    )
}

fun CartItemDto.toDomain(): CartItem {
    return CartItem(
        product = this.product.toDomain(),
        quantity = this.quantity
    )
}

fun CartResponse.toDomain(): Cart {
    return Cart(
        cart = this.cart.map { it.toDomain() }
    )
}

fun DetailsBondDto.toDomain(): DetailsBond {
    return DetailsBond(
        nameBond = this.nameBond,
        bondDescription = this.bondCooling,
        bondCooling = this.bondDescription
    )
}

fun EquipmentModelDto.toDomain(): EquipmentModel {
    return EquipmentModel(
        model = this.model,
        name = this.name
    )
}

fun DetailedProductResponse.toDomain(): ProductDetails {
    return ProductDetails(
        item = this.item.toDomain(),
        bonds = this.bonds.map { it.toDomain() },
        machines = this.machines.map { it.toDomain() },
        mounting = this.mounting?.toDomain()
    )
}

fun CatalogDto.toDomain(): Catalog {
    return Catalog(
        items = this.items.map { it.toDomain() },
        currentPage = this.currentPage,
        itemsPerPage = this.itemsPerPage,
        totalItems = this.totalItems,
        totalPages = this.totalPages
    )
}

fun CatalogParams.toQueryMap(page: Int): Map<String, String> =
    buildMap {
        searchCode?.let { put("search_code", it) }
        searchShape?.let { put("search_shape", it) }
        searchDimensions?.let { put("search_dimensions", it) }
        searchMachine?.let { put("search_machine", it) }

        bondIds?.takeIf { it.isNotEmpty() }?.let { put("bond_ids", it.joinToString(",")) }
        gridSizeIds?.takeIf { it.isNotEmpty() }?.let { put("grid_size_ids", it.joinToString(",")) }
        mountingIds?.takeIf { it.isNotEmpty() }?.let { put("mounting_ids", it.joinToString(",")) }

        put("category_id", categoryId.toString())
        put("page", page.toString())
        put("items_per_page", itemsPerPage.toString())
    }
