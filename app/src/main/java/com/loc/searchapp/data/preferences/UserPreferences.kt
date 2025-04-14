package com.loc.searchapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    internal object Keys {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

    suspend fun saveTokens(token: String, refreshToken: String) {
        dataStore.edit {
                it[Keys.TOKEN] = token
                it[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data.first()[Keys.TOKEN]
    }
    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[Keys.REFRESH_TOKEN]
    }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}