package com.dominio.bloommind.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
data class QuotaInfo(
    val count: Int,
    val windowStartMillis: Long
)
private val Context.chatQuotaDataStore: DataStore<Preferences> by preferencesDataStore(name = "chat_quota_prefs")

class ChatQuotaRepository(context: Context) {

    private val dataStore = context.chatQuotaDataStore
    companion object {
        const val MAX_QUOTA = 25
        private const val TWENTY_FOUR_HOURS_MS = 24 * 60 * 60 * 1000
    }

    private object PreferencesKeys {
        val MESSAGE_COUNT = intPreferencesKey("message_count")
        val WINDOW_START_MILLIS = longPreferencesKey("window_start_millis")
    }

    suspend fun canSendNow(): Boolean {
        val quotaInfo = getQuotaInfo()
        return quotaInfo.count < MAX_QUOTA
    }

    suspend fun incrementAndSave() {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0
            preferences[PreferencesKeys.MESSAGE_COUNT] = currentCount + 1
        }
    }

    suspend fun getQuotaInfo(): QuotaInfo {
        val currentMillis = System.currentTimeMillis()
        val preferences = dataStore.data.first()

        val windowStart = preferences[PreferencesKeys.WINDOW_START_MILLIS] ?: 0L
        val count = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0

        if (currentMillis - windowStart >= TWENTY_FOUR_HOURS_MS) {
            dataStore.edit { prefs ->
                prefs[PreferencesKeys.MESSAGE_COUNT] = 0
                prefs[PreferencesKeys.WINDOW_START_MILLIS] = currentMillis
            }
            return QuotaInfo(count = 0, windowStartMillis = currentMillis)
        }
        return QuotaInfo(count = count, windowStartMillis = windowStart)
    }
}