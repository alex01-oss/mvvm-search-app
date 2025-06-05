package com.loc.searchapp.core.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

    val tokenFlow: Flow<String?> = dataStore.data
        .map { prefs ->
            prefs[Keys.ACCESS_TOKEN]?.let {
                try {
                    EncryptionManager.decrypt(it)
                } catch (e: Exception) {
                    Log.e("TokenDecrypt", "Failed to decrypt access token", e)
                    null
                }
            }
        }
        .catch {
            Log.e("TokenFlow", "Error reading token", it)
            clearTokens()
            emit(null)
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
        return tokenFlow.first()
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