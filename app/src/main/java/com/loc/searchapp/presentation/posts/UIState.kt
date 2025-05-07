package com.loc.searchapp.presentation.posts

sealed class UIState {
    object Loading : UIState()
    data class Error(val message: String) : UIState()
    object Success : UIState()
}