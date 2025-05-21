package com.loc.searchapp.core.domain.model.common

import androidx.annotation.DrawableRes

data class BottomNavItem(
    @DrawableRes val icon: Int,
    val text: String
)
