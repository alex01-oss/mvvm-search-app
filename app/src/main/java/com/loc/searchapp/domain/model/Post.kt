package com.loc.searchapp.domain.model

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String
)