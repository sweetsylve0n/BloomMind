package com.dominio.bloommind.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.emotionDataStore: DataStore<Preferences> by preferencesDataStore(name = "emotion_checkin")

class EmotionRepository(context: Context) {

    private val dataStore = context.emotionDataStore

    private object Keys {
        val LAST_CHECKIN_TIMESTAMP = longPreferencesKey("last_checkin_timestamp")
        val SAVED_EMOTIONS = stringSetPreferencesKey("saved_emotions")
    }

    suspend fun canDoCheckIn(): Boolean {
        val lastTimestamp = dataStore.data.map { it[Keys.LAST_CHECKIN_TIMESTAMP] ?: 0L }.first()
        val diff = System.currentTimeMillis() - lastTimestamp
        return TimeUnit.MILLISECONDS.toHours(diff) >= 24
    }

    suspend fun saveCheckIn(emotionIds: Set<Int>) {
        dataStore.edit { preferences ->
            preferences[Keys.LAST_CHECKIN_TIMESTAMP] = System.currentTimeMillis()
            // Convert Int set to String set for DataStore
            preferences[Keys.SAVED_EMOTIONS] = emotionIds.map { it.toString() }.toSet()
        }
    }

    suspend fun getTodaysEmotions(): Set<Int> {
        return dataStore.data.map { preferences ->
            preferences[Keys.SAVED_EMOTIONS]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }.first()
    }
}
