package com.loc.searchapp.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        val encryptedAccess = EncryptionManager.encrypt(accessToken)
        val encryptedRefresh = EncryptionManager.encrypt(refreshToken)
        dataStore.edit {
            it[Keys.ACCESS_TOKEN] = encryptedAccess
            it[Keys.REFRESH_TOKEN] = encryptedRefresh
        }
    }

    suspend fun getAccessToken(): String? {
        return runCatching {
            val encrypted = dataStore.data.first()[Keys.ACCESS_TOKEN]
            encrypted?.let { EncryptionManager.decrypt(it) }
        }.getOrNull()
    }

    suspend fun getRefreshToken(): String? {
        return runCatching {
            val encrypted = dataStore.data.first()[Keys.REFRESH_TOKEN]
            encrypted?.let { EncryptionManager.decrypt(it) }
        }.getOrNull()
    }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}