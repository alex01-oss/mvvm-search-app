package com.loc.searchapp.domain.usecases.posts

data class PostsUseCases(
    val createPost: CreatePost,
    val editPost: EditPost,
    val deletePost: DeletePost,
    val uploadImage: UploadImage,
    val getPost: GetPost,
    val getAllPosts: GetAllPosts
)
