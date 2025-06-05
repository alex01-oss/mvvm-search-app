package com.loc.searchapp.feature.post_details.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDate(): String {
    return try {
        val input = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val output = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("uk"))
        ZonedDateTime.parse(this, input).format(output)
    } catch (_: Exception) {
        this.take(10)
    }
}