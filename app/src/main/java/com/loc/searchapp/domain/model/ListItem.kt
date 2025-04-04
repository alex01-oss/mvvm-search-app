package com.loc.searchapp.domain.model

sealed class ListItem {
    data class CatalogListItem(val product: Product): ListItem()
    data class CartListItem(val cartItem: CartItem): ListItem()
}