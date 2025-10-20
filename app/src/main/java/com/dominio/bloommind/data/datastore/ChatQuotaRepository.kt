package com.dominio.bloommind.data.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

// Objeto para almacenar la información de la cuota actual
data class QuotaInfo(
    val count: Int,
    val windowStartMillis: Long
)

// Creamos un DataStore específico para la cuota del chat
private val Context.chatQuotaDataStore: DataStore<Preferences> by preferencesDataStore(name = "chat_quota_prefs")

class ChatQuotaRepository(context: Context) {

    private val dataStore = context.chatQuotaDataStore

    // Constantes para la lógica de la cuota
    companion object {
        const val MAX_QUOTA = 25
        private const val TWENTY_FOUR_HOURS_MS = 24 * 60 * 60 * 1000
    }

    private object PreferencesKeys {
        val MESSAGE_COUNT = intPreferencesKey("message_count")
        val WINDOW_START_MILLIS = longPreferencesKey("window_start_millis")
    }

    // Función principal para verificar si el usuario puede enviar un mensaje
    suspend fun canSendNow(): Boolean {
        val quotaInfo = getQuotaInfo()
        return quotaInfo.count < MAX_QUOTA
    }

    // Incrementa el contador y guarda el estado
    suspend fun incrementAndSave() {
        dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0
            preferences[PreferencesKeys.MESSAGE_COUNT] = currentCount + 1
        }
    }

    // Obtiene la información de la cuota, reiniciándola si han pasado 24h
    suspend fun getQuotaInfo(): QuotaInfo {
        val currentMillis = System.currentTimeMillis()
        val preferences = dataStore.data.first() // Leemos el estado actual una vez

        val windowStart = preferences[PreferencesKeys.WINDOW_START_MILLIS] ?: 0L
        val count = preferences[PreferencesKeys.MESSAGE_COUNT] ?: 0

        // Comprobamos si el período de 24h ha expirado
        if (currentMillis - windowStart >= TWENTY_FOUR_HOURS_MS) {
            // El tiempo ha expirado, reiniciamos el contador y la ventana de tiempo
            dataStore.edit { prefs ->
                prefs[PreferencesKeys.MESSAGE_COUNT] = 0
                prefs[PreferencesKeys.WINDOW_START_MILLIS] = currentMillis
            }
            // Devolvemos el estado reiniciado
            return QuotaInfo(count = 0, windowStartMillis = currentMillis)
        }

        // Si no ha expirado, devolvemos los valores actuales
        return QuotaInfo(count = count, windowStartMillis = windowStart)
    }
}