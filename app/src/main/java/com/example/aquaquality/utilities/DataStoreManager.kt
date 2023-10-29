package com.example.aquaquality.utilities

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {
    val dataStore = context.dataStore

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SETTINGS")
        val darkModeKey = booleanPreferencesKey("DARK_MODE_KEY")
        val languageKey = stringPreferencesKey("LANGUAGE_KEY")
    }

    suspend fun setTheme(isDarkMode: Boolean) {
        dataStore.edit { pref ->
            pref[darkModeKey] = isDarkMode
        }
    }

    fun getTheme(): Flow<Boolean>{
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {pref ->
                val uiMode = pref[darkModeKey] ?: false
                uiMode
            }
    }

    suspend fun setLanguage(languageCode: String) {
        dataStore.edit { pref ->
            pref[languageKey] = languageCode
        }
    }

    fun getLanguage(): Flow<String>{
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {pref ->
                val uiMode = pref[languageKey] ?: "en"
                uiMode
            }
    }
}