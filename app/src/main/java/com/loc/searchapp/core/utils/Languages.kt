package com.loc.searchapp.core.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.loc.searchapp.R

data class AppLanguage(
    @StringRes val titleRes: Int,
    val code: String,
    @DrawableRes val iconRes: Int
)

object Languages {
    val list = listOf(
        AppLanguage(R.string.system_language, "system", R.drawable.globe),
        AppLanguage(R.string.english, "en", R.drawable.flag_uk),
        AppLanguage(R.string.ukrainian, "uk", R.drawable.flag_ukraine)
    )
}
