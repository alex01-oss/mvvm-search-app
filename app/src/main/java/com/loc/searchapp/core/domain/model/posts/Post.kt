package com.loc.searchapp.core.domain.model.posts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: Int
) : Parcelable