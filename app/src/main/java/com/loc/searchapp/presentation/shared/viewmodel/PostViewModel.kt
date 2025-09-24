package com.loc.searchapp.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.domain.model.posts.EditPostData
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostData
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.model.posts.UploadData
import com.loc.searchapp.core.domain.model.posts.UploadResult
import com.loc.searchapp.core.domain.usecases.posts.PostsUseCases
import com.loc.searchapp.presentation.post_editor.model.PostEditorState
import com.loc.searchapp.presentation.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postsUseCases: PostsUseCases
) : ViewModel() {

    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val postsState = _postsState.asStateFlow()

    private val _postEditorState = MutableStateFlow<PostEditorState>(PostEditorState.CreateMode)
    val postEditorState: StateFlow<PostEditorState> = _postEditorState.asStateFlow()

    private val _postDetailsState = MutableStateFlow<UiState<Post>>(UiState.Loading)
    val postDetailsState = _postDetailsState.asStateFlow()

    private val _postActionState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val postActionState = _postActionState.asStateFlow()

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
                val post = postsUseCases.getPost(PostId(postId))
                _postEditorState.value = PostEditorState.EditMode(post)

            } catch (e: Exception) {
                _postEditorState.value =
                    PostEditorState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
        resetPostActionState()
    }

    fun getPostById(postId: Int) {
        viewModelScope.launch {
            _postDetailsState.value = UiState.Loading
            try {
                val post = postsUseCases.getPost(PostId(postId))
                _postDetailsState.value = UiState.Success(post)
            } catch (e: Exception) {
                _postDetailsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            try {
                val posts = postsUseCases.getAllPosts()
                _postsState.value =
                    if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
            } catch (e: Exception) {
                _postsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun createPost(title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                postsUseCases.createPost(PostData(title, content, imageUrl))
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value =
                    UiState.Error(e.localizedMessage ?: "Failed to create post")
            }
        }
    }

    fun editPost(postId: Int, title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                postsUseCases.editPost(EditPostData(postId, title, content, imageUrl))
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value =
                    UiState.Error(e.localizedMessage ?: "Failed to update post")
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                postsUseCases.deletePost(PostId(postId))
                _postActionState.value = UiState.Success(Unit)
                loadPosts()
            } catch (e: Exception) {
                _postActionState.value =
                    UiState.Error(e.localizedMessage ?: "Failed to delete post")
            }
        }
    }

    fun uploadImage(file: File, onResult: (UploadResult?) -> Unit) {
        viewModelScope.launch {
            onResult(postsUseCases.uploadImage(UploadData(file)))
        }
    }

    fun resetPostActionState() {
        _postActionState.value = UiState.Empty
    }
}