package com.loc.searchapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.loc.searchapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Lorem ipsum dolor sit amet.",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend risus id.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Lorem ipsum dolor sit amet.",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend risus id.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Lorem ipsum dolor sit amet.",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend risus id.",
        image = R.drawable.onboarding3
    )
)