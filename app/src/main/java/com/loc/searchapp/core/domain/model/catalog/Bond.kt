package com.loc.searchapp.core.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Bond(
    @SerialName("name_bond") val nameBond: String,
    @SerialName("bond_description") val bondDescription: String,
    @SerialName("bond_cooling") val bondCooling: String
) : Parcelable