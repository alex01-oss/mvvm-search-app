package com.loc.searchapp.core.utils

import com.loc.searchapp.BuildConfig

object Constants {
    const val USER_SETTINGS = "userSettings"
    const val APP_ENTRY = "appEntry"

    val BASE_URL: String
        get() = BuildConfig.BASE_URL
}