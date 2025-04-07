package com.loc.searchapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.loc.searchapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(internal val dataStore: DataStore<Preferences>) {

    internal object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

    val user: Flow<User?> = dataStore.data.map { preferences ->
        val id = preferences[Keys.USER_ID]
        val username = preferences[Keys.USERNAME]
        val email = preferences[Keys.EMAIL]
        val token = preferences[Keys.TOKEN]
        val refreshToken = preferences[Keys.REFRESH_TOKEN]

        if (
            id != null &&
            username != null &&
            email != null &&
            token != null &&
            refreshToken != null
        ) {
            User(id, username, email, token, refreshToken)
        } else {
            null
        }
    }

    suspend fun saveUser(user: User?) {
        dataStore.edit { preferences ->
            if(user != null) {
                preferences[Keys.USER_ID] = user.id
                preferences[Keys.USERNAME] = user.username
                preferences[Keys.EMAIL] = user.email
                preferences[Keys.TOKEN] = user.token
                preferences[Keys.REFRESH_TOKEN] = user.refreshToken
            }
        }
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }

    suspend fun getToken(): String? {
        return dataStore.data.first()[Keys.TOKEN]
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[Keys.REFRESH_TOKEN]
    }

    suspend fun saveToken(token: String) {
        val currentUser = user.first()
        if (currentUser != null) {
            saveUser(currentUser.copy(token = token))
        }
    }
}