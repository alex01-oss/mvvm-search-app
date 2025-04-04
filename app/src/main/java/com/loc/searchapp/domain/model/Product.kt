package com.loc.searchapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val code: String,
    val dimensions: String,
    val images: String,
    val shape: String
): Parcelable