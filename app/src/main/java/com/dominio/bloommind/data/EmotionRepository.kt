package com.dominio.bloommind.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.emotionDataStore: DataStore<Preferences> by preferencesDataStore(name = "emotion_checkin")

class EmotionRepository(context: Context) {

    private val dataStore = context.emotionDataStore

    private object Keys {
        val SAVED_EMOTIONS = stringSetPreferencesKey("saved_emotions")
        val LAST_CHECKIN_DATE = stringPreferencesKey("last_checkin_date")
        val HISTORY = stringPreferencesKey("emotions_history_v2")
    }

    private fun getTimestamp(): String {
        // Formato completo para permitir múltiples check-ins
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
    
    private fun getTodayDate(): String {
         return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun parseHistory(serialized: String?): Map<String, Set<Int>> {
        if (serialized.isNullOrBlank()) return emptyMap()
        return serialized.splitToSequence('|')
            .mapNotNull { entry ->
                // Usamos # como separador para no conflictos con la hora (HH:mm:ss)
                val parts = entry.split("#")
                if (parts.size != 2) return@mapNotNull null
                val date = parts[0]
                val ids = parts[1].splitToSequence(',').mapNotNull { it.toIntOrNull() }.toSet()
                date to ids
            }
            .toMap()
    }

    private fun serializeHistory(map: Map<String, Set<Int>>): String {
        return map.entries.joinToString(separator = "|") { (date, set) ->
            val ids = set.joinToString(separator = ",")
            "$date#$ids" // Usamos # como separador
        }
    }

    suspend fun saveCheckIn(emotionIds: Set<Int>) {
        val timestamp = getTimestamp()
        val today = getTodayDate()

        dataStore.edit { preferences ->
            preferences[Keys.SAVED_EMOTIONS] = emotionIds.map { it.toString() }.toSet()
            preferences[Keys.LAST_CHECKIN_DATE] = today

            val existing = preferences[Keys.HISTORY]
            val map = parseHistory(existing).toMutableMap()
            
            map[timestamp] = emotionIds

            // Ordenamos por fecha descendente (más reciente primero) y guardamos solo los últimos 5
            val kept = map.toSortedMap(compareByDescending { it }).entries.take(5).associate { it.toPair() }
            
            preferences[Keys.HISTORY] = serializeHistory(kept)
        }
    }

    suspend fun getTodaysEmotions(): Set<Int> {
        val today = getTodayDate()
         return dataStore.data.map { preferences ->
            val savedDate = preferences[Keys.LAST_CHECKIN_DATE]
            if (savedDate == today) {
                preferences[Keys.SAVED_EMOTIONS]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
            } else {
                emptySet()
            }
        }.first()
    }

    suspend fun getLastNCheckIns(n: Int = 5): List<Pair<String, Set<Int>>> {
        return dataStore.data.map { preferences ->
            val history = parseHistory(preferences[Keys.HISTORY])
            history.entries
                .sortedByDescending { it.key }
                .take(n)
                .map { it.key to it.value }
        }.first()
    }
}
