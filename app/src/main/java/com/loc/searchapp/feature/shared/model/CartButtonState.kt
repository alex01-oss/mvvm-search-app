package com.loc.searchapp.feature.shared.model

sealed class CartButtonState {
    object Add : CartButtonState()
    object Remove : CartButtonState()
    object Loading : CartButtonState()
}