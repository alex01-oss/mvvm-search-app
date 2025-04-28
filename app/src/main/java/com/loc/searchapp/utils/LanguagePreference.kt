package com.loc.searchapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object LanguagePreference {
    private const val PREF_NAME = "language_preferences"
    private const val LANGUAGE_KEY = "selected_language"
    private const val LANGUAGE_DEFAULT = "system"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getLanguage(context: Context): String {
        return getPreferences(context).getString(LANGUAGE_KEY, LANGUAGE_DEFAULT) ?: LANGUAGE_DEFAULT
    }

    fun setLanguage(context: Context, languageCode: String) {
        getPreferences(context).edit {
            putString(LANGUAGE_KEY, languageCode)
        }
    }
}