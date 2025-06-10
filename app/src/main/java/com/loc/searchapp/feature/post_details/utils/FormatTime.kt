package com.loc.searchapp.feature.post_details.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("uk"))

        val date = inputFormat.parse(this)
        outputFormat.format(date!!)
    } catch (_: Exception) {
        this.take(10)
    }
}