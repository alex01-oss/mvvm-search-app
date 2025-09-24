package com.loc.searchapp.presentation.post_editor.model

import com.loc.searchapp.core.domain.model.posts.Post

sealed class PostEditorState {
    object CreateMode : PostEditorState()
    object Loading : PostEditorState()
    data class EditMode(val post: Post) : PostEditorState()
    data class Error(val message: String) : PostEditorState()
}