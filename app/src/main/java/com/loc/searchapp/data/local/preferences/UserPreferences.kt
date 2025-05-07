package com.loc.searchapp.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[Keys.ACCESS_TOKEN] = accessToken
            it[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? {
        return runCatching {
            dataStore.data.first()[Keys.ACCESS_TOKEN]
        }.getOrNull()
    }

    suspend fun getRefreshToken(): String? {
        return runCatching {
            dataStore.data.first()[Keys.REFRESH_TOKEN]
        }.getOrNull()
    }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}