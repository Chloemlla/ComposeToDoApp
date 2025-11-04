package com.example.todoapp.repository

// SettingsRepository.kt
import com.example.todoapp.data.PreferencesKeys // Import the keys from the data package
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    // Read the dark mode setting
    val isDarkModeFlow = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_DARK_MODE] ?: false // default value is false
    }

    // Write the dark mode setting
    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    // You would add your sort order logic here as well
    // ...
}