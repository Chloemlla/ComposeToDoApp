package com.example.todoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// 1. Extension property to create the singleton DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "todo_settings" // A unique name for your DataStore file
)

// 2. Object to hold the keys
object PreferencesKeys {
    // Example key to store a boolean for a 'dark theme' setting
    val IS_DARK_MODE = androidx.datastore.preferences.core.booleanPreferencesKey("is_dark_mode")
    // Example key for your todo app's sort order
    val SORT_ORDER = androidx.datastore.preferences.core.stringPreferencesKey("sort_order")
}