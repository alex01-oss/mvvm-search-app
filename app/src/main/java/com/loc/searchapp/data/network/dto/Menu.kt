package com.loc.searchapp.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(
    @SerialName("sharpening") val sharpeningTool: MenuCategory,
    @SerialName("axial_tool") val axialTool: MenuCategory,
    @SerialName("grinding_tool") val grindingTool: MenuCategory,
    @SerialName("construction_tool") val constructionTool: MenuCategory,
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
