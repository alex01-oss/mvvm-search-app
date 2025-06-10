package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.remote.dto.CreatePostRequest
import com.loc.searchapp.core.data.remote.dto.EditPostRequest
import com.loc.searchapp.core.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.usecases.posts.PostsUseCases
import com.loc.searchapp.feature.post_editor.model.PostEditorState
import com.loc.searchapp.feature.shared.model.UiState
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

    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val postsState: StateFlow<UiState<List<Post>>> = _postsState.asStateFlow()

    private val _postEditorState = MutableStateFlow<PostEditorState>(PostEditorState.CreateMode)
    val postEditorState: StateFlow<PostEditorState> = _postEditorState.asStateFlow()

    private val _postDetailsState = MutableStateFlow<UiState<Post>>(UiState.Loading)
    val postDetailsState: StateFlow<UiState<Post>> = _postDetailsState.asStateFlow()

    private val _postActionState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val postActionState: StateFlow<UiState<Unit>> = _postActionState.asStateFlow()

    init {
        loadPosts()
    }

    fun initCreateMode() {
        _postEditorState.value = PostEditorState.CreateMode
        resetPostActionState()
    }

    fun initEditMode(postId: Int) {
        _postEditorState.value = PostEditorState.Loading
        viewModelScope.launch {
            try {
                val response = postsUseCases.getPost(postId)
                if (response.isSuccessful && response.body() != null) {
                    _postEditorState.value = PostEditorState.EditMode(response.body()!!.toDomain())
                } else {
                    _postEditorState.value = PostEditorState.Error("Post not found")
                }
            } catch (e: Exception) {
                _postEditorState.value = PostEditorState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
        resetPostActionState()
    }

    fun getPostById(postId: Int?) {
        viewModelScope.launch {
            _postDetailsState.value = UiState.Loading
            try {
                val response = postsUseCases.getPost(postId)
                if (response.isSuccessful && response.body() != null) {
                    _postDetailsState.value = UiState.Success(response.body()!!.toDomain())
                } else {
                    _postDetailsState.value = UiState.Error("Post not found")
                }
            } catch (e: Exception) {
                _postDetailsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            try {
                val response = postsUseCases.getAllPosts()
                if (response.isSuccessful) {
                    val posts = response.body()?.map { it.toDomain() }.orEmpty()
                    _postsState.value = if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
                } else {
                    _postsState.value = UiState.Error("Failed to load posts")
                }
            } catch (e: Exception) {
                _postsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun createPost(title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                val request = CreatePostRequest(title, content, imageUrl)
                postsUseCases.createPost(request)
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value = UiState.Error(e.localizedMessage ?: "Failed to create post")
            }
        }
    }

    fun editPost(postId: Int, title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                val request = EditPostRequest(title, content, imageUrl)
                postsUseCases.editPost(postId, request)
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value = UiState.Error(e.localizedMessage ?: "Failed to update post")
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                postsUseCases.deletePost(postId)
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value = UiState.Error(e.localizedMessage ?: "Failed to delete post")
            }
        }
    }

    fun uploadImage(file: File, onResult: (ImageUploadResponse?) -> Unit) {
        viewModelScope.launch {
            val imagePart = prepareFilePart("file", file)
            val response = postsUseCases.uploadImage(imagePart)
            onResult(if (response.isSuccessful) response.body() else null)
        }
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun PostResponse.toDomain(): Post {
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

    fun resetPostActionState() {
        _postActionState.value = UiState.Empty
    }
}