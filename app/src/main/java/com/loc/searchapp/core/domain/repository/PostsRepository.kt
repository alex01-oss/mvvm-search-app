package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.domain.model.posts.DeletePostResult
import com.loc.searchapp.core.domain.model.posts.EditPostData
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostData
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.model.posts.UploadData
import com.loc.searchapp.core.domain.model.posts.UploadResult

interface PostsRepository {
    suspend fun createPost(data: PostData): Post
    suspend fun deletePost(data: PostId): DeletePostResult
    suspend fun editPost(data: EditPostData): Post
    suspend fun getAllPosts(): List<Post>
    suspend fun getPostById(data: PostId): Post
    suspend fun uploadImage(data: UploadData): UploadResult
}