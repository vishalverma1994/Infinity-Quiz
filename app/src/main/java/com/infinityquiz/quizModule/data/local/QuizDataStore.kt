package com.infinityquiz.quizModule.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val LAST_FETCH_TIME_KEY = longPreferencesKey("last_fetch_time")

    suspend fun updateLastFetchTime(newTime: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_FETCH_TIME_KEY] = newTime
        }
    }

    suspend fun getLastFetchTime(): Long {
        return dataStore.data.map { preferences ->
            preferences[LAST_FETCH_TIME_KEY] ?: 0
        }.first()
    }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}