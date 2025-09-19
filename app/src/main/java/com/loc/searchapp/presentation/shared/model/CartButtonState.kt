package com.loc.searchapp.presentation.shared.model

sealed class CartButtonState {
    object Add : CartButtonState()
    object Remove : CartButtonState()
    object Loading : CartButtonState()
}