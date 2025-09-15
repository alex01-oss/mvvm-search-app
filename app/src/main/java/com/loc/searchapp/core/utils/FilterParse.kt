package com.loc.searchapp.core.utils

fun getApiFieldName(categoryKey: String): String {
    return when (categoryKey) {
        "bonds" -> "bond_ids"
        "grids" -> "grid_size_ids"
        "mountings" -> "mounting_ids"
        else -> categoryKey + "_ids"
    }
}

fun getDisplayField(categoryKey: String): String {
    return when (categoryKey) {
        "bonds" -> "name_bond"
        "grids" -> "grid_size"
        "mountings" -> "mm"
        else -> "name"
    }
}
