package com.loc.searchapp.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = if (language == "system") {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale(language)
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
