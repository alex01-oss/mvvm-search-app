package com.loc.searchapp.data.network.dto

import com.loc.searchapp.domain.model.Product

data class Catalog(
    val current_page: Int = 1,
    val items: List<Product> = emptyList(),
    val items_per_page: Int = 10,
    val total_items: Int = 0,
    val total_pages: Int = 1
)
