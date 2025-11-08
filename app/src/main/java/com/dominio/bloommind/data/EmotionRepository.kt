package com.dominio.bloommind.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.emotionDataStore: DataStore<Preferences> by preferencesDataStore(name = "emotion_checkin")

class EmotionRepository(context: Context) {

    private val dataStore = context.emotionDataStore

    private object Keys {
        // The timestamp key is removed, we only care about the last saved set.
        val SAVED_EMOTIONS = stringSetPreferencesKey("saved_emotions")
    }

    // The canDoCheckIn function is removed to allow overwriting the check-in at any time.

    /**
     * Saves or overwrites the current set of checked-in emotions.
     */
    suspend fun saveCheckIn(emotionIds: Set<Int>) {
        dataStore.edit { preferences ->
            preferences[Keys.SAVED_EMOTIONS] = emotionIds.map { it.toString() }.toSet()
        }
    }

    /**
     * Retrieves the latest set of emotions saved by the user.
     */
    suspend fun getTodaysEmotions(): Set<Int> {
        return dataStore.data.map { preferences ->
            preferences[Keys.SAVED_EMOTIONS]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }.first()
    }
}
