package com.loc.searchapp.core.data.mappers

import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams

fun AutocompleteParams.toQueryMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()

    map["q"] = query

    categoryId?.let { map["category_id"] = it.toString() }
    searchCode?.let { map["search_code"] = it }
    searchShape?.let { map["search_shape"] = it }
    searchDimensions?.let { map["search_dimensions"] = it }
    searchMachine?.let { map["search_machine"] = it }

    bondIds.takeIf { it.isNotEmpty() }?.let {
        map["bond_ids"] = it.joinToString(",")
    }
    gridSizeIds.takeIf { it.isNotEmpty() }?.let {
        map["grid_size_ids"] = it.joinToString(",")
    }
    mountingIds.takeIf { it.isNotEmpty() }?.let {
        map["mounting_ids"] = it.joinToString(",")
    }

    return map
}