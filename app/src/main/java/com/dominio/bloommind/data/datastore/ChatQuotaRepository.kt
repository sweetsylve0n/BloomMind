package com.dominio.bloommind.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class QuotaInfo(
    val count: Int,
    val date: String
)

private val Context.chatQuotaDataStore: DataStore<Preferences> by preferencesDataStore(name = "chat_quota_prefs")

class ChatQuotaRepository(context: Context) {

    private val dataStore = context.chatQuotaDataStore

    companion object {
        const val MAX_QUOTA = 25
    }

    private object PreferencesKeys {
        val MESSAGE_COUNT = intPreferencesKey("message_count")
        val LAST_CHAT_DATE = stringPreferencesKey("last_chat_date")
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun incrementAndSave() {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0
            preferences[PreferencesKeys.MESSAGE_COUNT] = currentCount + 1
            // Also update the date to today, in case it was the first message of the day
            preferences[PreferencesKeys.LAST_CHAT_DATE] = getCurrentDateString()
        }
    }

    suspend fun getQuotaInfo(): QuotaInfo {
        val preferences = dataStore.data.first()
        val today = getCurrentDateString()

        val lastDate = preferences[PreferencesKeys.LAST_CHAT_DATE] ?: ""
        val count = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0

        // If the saved date is not today, reset the counter.
        if (lastDate != today) {
            dataStore.edit {
                it[PreferencesKeys.MESSAGE_COUNT] = 0
            }
            return QuotaInfo(count = 0, date = today)
        }
        return QuotaInfo(count = count, date = lastDate)
    }
}
