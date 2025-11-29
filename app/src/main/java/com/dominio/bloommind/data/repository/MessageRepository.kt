package com.dominio.bloommind.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.messageDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_messages")

class MessageRepository(private val context: Context) {

    companion object {
        val FUTURE_SELF_MESSAGE = stringPreferencesKey("future_self_message")
        val HAS_BAD_DAY_FLAG = booleanPreferencesKey("has_bad_day_flag")
    }

    val futureMessage: Flow<String?> = context.messageDataStore.data
        .map { preferences ->
            preferences[FUTURE_SELF_MESSAGE]
        }

    val hasBadDay: Flow<Boolean> = context.messageDataStore.data
        .map { preferences ->
            preferences[HAS_BAD_DAY_FLAG] ?: false
        }

    suspend fun saveFutureMessage(message: String) {
        context.messageDataStore.edit { preferences ->
            preferences[FUTURE_SELF_MESSAGE] = message
        }
    }

    suspend fun setBadDayFlag(hasBadDay: Boolean) {
        context.messageDataStore.edit { preferences ->
            preferences[HAS_BAD_DAY_FLAG] = hasBadDay
        }
    }
}
