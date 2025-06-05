package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(
    val categories: List<MenuCategory>
)

@Serializable
data class MenuCategory(
    val title: String,
    val items: List<MenuItem> = emptyList()
)

@Serializable
data class MenuItem(
    val text: String,
    val items: List<MenuSubItem> = emptyList()
)

@Serializable
data class MenuSubItem(
    val text: String,
    val type: String? = null,
    val url: String? = null,
    val searchType: String? = null
)
