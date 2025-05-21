package com.loc.searchapp.core.domain.model.posts

import com.loc.searchapp.core.utils.Constants.CATALOG_URL
import java.io.File

data class PostFormState(
    val title: String = "",
    val imageUrl: String? = null,
    val imageFile: File? = null,
    val content: String = ""
) {
    val hasImage: Boolean get() = imageUrl != null || imageFile != null
    val previewUri: String?
        get() = when {
            imageFile != null -> imageFile.toURI().toString()
            imageUrl != null -> "$CATALOG_URL$imageUrl"
            else -> null
        }
}