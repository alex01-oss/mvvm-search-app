package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateInfo(
    val latestVersion: String,
    val versionCode: Int,
    val downloadUrl: String,
    val changelog: String,
    val forceUpdate: Boolean
)