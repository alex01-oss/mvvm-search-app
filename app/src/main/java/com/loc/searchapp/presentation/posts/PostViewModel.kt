package com.loc.searchapp.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.remote.dto.CreatePostRequest
import com.loc.searchapp.data.remote.dto.EditPostRequest
import com.loc.searchapp.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.domain.usecases.posts.PostsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postsUseCases: PostsUseCases
) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    suspend fun loadPosts() {
        viewModelScope.launch {
            val response = postsUseCases.getAllPosts()

            if (response.isSuccessful) {
                if (response.body() != null) {
                    response.body()?.let { list ->
                        _posts.value = list.map { it.toDomain() }
                    }
                }
            } else {
                _posts.value = emptyList()
            }
        }
    }

    suspend fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    suspend fun uploadImage(file: File, onResult: (ImageUploadResponse?) -> Unit) {
        viewModelScope.launch {
            val imagePart = prepareFilePart("file", file)

            val response = postsUseCases.uploadImage(imagePart)

            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
    }

    suspend fun createPost(title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            val request = CreatePostRequest(title, content, imageUrl)
            postsUseCases.createPost(request)
            loadPosts()
        }
    }

    suspend fun editPost(postId: Int, title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            val request = EditPostRequest(title, content, imageUrl)
            postsUseCases.editPost(postId, request)
            loadPosts()
        }
    }

    suspend fun deletePost(postId: Int) {
        viewModelScope.launch {
            postsUseCases.deletePost(postId)
            loadPosts()
        }
    }

    suspend fun getPostById(postId: Int?): Post? {
        val response = postsUseCases.getPost(postId)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    suspend fun PostResponse.toDomain(): Post {
        return Post(
            id = this.id,
            title = this.title,
            content = this.content,
            imageUrl = this.image.toString(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            userId = this.userId,
        )
    }
}