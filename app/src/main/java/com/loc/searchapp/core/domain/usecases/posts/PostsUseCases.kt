package com.loc.searchapp.core.domain.usecases.posts

import jakarta.inject.Inject

data class PostsUseCases @Inject constructor(
    val createPost: CreatePost,
    val editPost: EditPost,
    val deletePost: DeletePost,
    val uploadImage: UploadImage,
    val getPost: GetPost,
    val getAllPosts: GetAllPosts
)
