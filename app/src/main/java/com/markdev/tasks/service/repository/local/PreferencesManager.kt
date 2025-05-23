package com.markdev.tasks.service.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    suspend fun store(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }
    }


    suspend fun remove(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences -> preferences.remove(preferencesKey) }
    }


    suspend fun get(key: String): String {
        val preferencesKey = stringPreferencesKey(key)
        val data = context.dataStore.data.first()

        return data[preferencesKey] ?: ""
    }

}