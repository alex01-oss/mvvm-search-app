package com.loc.searchapp.data.local.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.loc.searchapp.domain.manager.LocalUserManger
import com.loc.searchapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
) : LocalUserManger {
    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] == true
        }
    }

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.USER_SETTINGS
)

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
}