package com.loc.searchapp.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.domain.model.catalog.Limit
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

    private val _recentPostsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val recentPostsState = _recentPostsState.asStateFlow()

    private val _allPostsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val allPostsState = _allPostsState.asStateFlow()

    private val _postEditorState = MutableStateFlow<PostEditorState>(PostEditorState.CreateMode)
    val postEditorState: StateFlow<PostEditorState> = _postEditorState.asStateFlow()

    private val _postDetailsState = MutableStateFlow<UiState<Post>>(UiState.Loading)
    val postDetailsState = _postDetailsState.asStateFlow()

    private val _postActionState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val postActionState = _postActionState.asStateFlow()

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

    fun loadRecentPosts() {
        viewModelScope.launch {
            _recentPostsState.value = UiState.Loading
            try {
                val posts = postsUseCases.getAllPosts(Limit(3))
                _recentPostsState.value =
                    if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
            } catch (e: Exception) {
                _recentPostsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loadAllPosts() {
        viewModelScope.launch {
            _allPostsState.value = UiState.Loading
            try {
                val posts = postsUseCases.getAllPosts(null)
                _allPostsState.value =
                    if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
            } catch (e: Exception) {
                _allPostsState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun createPost(title: String, content: String, imageUrl: String?) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading
            try {
                postsUseCases.createPost(PostData(title, content, imageUrl))
                _postActionState.value = UiState.Success(Unit)
                loadAllPosts()
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
                loadAllPosts()
            } catch (e: Exception) {
                _postActionState.value =
                    UiState.Error(e.localizedMessage ?: "Failed to update post")
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            _postActionState.value = UiState.Loading

            val currentAllPosts = _allPostsState.value as? UiState.Success
            val currentRecentPosts = _recentPostsState.value as? UiState.Success

            currentAllPosts?.let { success ->
                val updated = success.data.filterNot { it.id == postId }
                _allPostsState.value = if (updated.isEmpty()) UiState.Empty
                    else UiState.Success(updated)
            }

            currentRecentPosts?.let { success ->
                val updated = success.data.filterNot { it.id == postId }
                _recentPostsState.value = if (updated.isEmpty()) UiState.Empty
                    else UiState.Success(updated)
            }

            try {
                postsUseCases.deletePost(PostId(postId))
                _postActionState.value = UiState.Success(Unit)
                loadAllPosts()
            } catch (e: Exception) {
                currentAllPosts?.let { _allPostsState.value = it }
                currentRecentPosts?.let { _recentPostsState.value = it }

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