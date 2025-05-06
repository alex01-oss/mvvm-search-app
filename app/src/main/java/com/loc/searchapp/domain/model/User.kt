package com.loc.searchapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val id: String,
    val username: String,
    val email: String,
) : Parcelable
