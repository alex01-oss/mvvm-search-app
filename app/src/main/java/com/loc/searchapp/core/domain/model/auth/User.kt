package com.loc.searchapp.core.domain.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val id: String,
    @SerialName("full_name") val fullname: String,
    val email: String,
    val phone: String,
    val role: String
) : Parcelable