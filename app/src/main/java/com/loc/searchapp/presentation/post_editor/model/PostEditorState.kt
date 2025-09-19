package com.loc.searchapp.presentation.post_editor.model

import com.loc.searchapp.core.data.remote.dto.PostResponse

sealed class PostEditorState {
    object CreateMode : PostEditorState()
    object Loading : PostEditorState()
    data class EditMode(val post: PostResponse) : PostEditorState()
    data class Error(val message: String) : PostEditorState()
}