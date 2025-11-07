package com.dominio.bloommind.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private val Context.requestQuotaDataStore: DataStore<Preferences> by preferencesDataStore(name = "request_quotas")

class RequestQuotaRepository(context: Context) {

    private val dataStore = context.requestQuotaDataStore

    private object Keys {
        val LAST_AFFIRMATION_TIMESTAMP = longPreferencesKey("last_affirmation_timestamp")
        val CACHED_AFFIRMATION = stringPreferencesKey("cached_affirmation")
        val AFFIRMATION_IMAGE_INDEX = intPreferencesKey("affirmation_image_index")
        val LAST_ADVICE_TIMESTAMP = longPreferencesKey("last_advice_timestamp")
        val CACHED_ADVICE = stringPreferencesKey("cached_advice")
    }

    suspend fun canFetchAffirmation(): Boolean {
        val lastTimestamp = dataStore.data.map { it[Keys.LAST_AFFIRMATION_TIMESTAMP] ?: 0L }.first()
        return has24HoursPassed(lastTimestamp)
    }

    suspend fun getCachedAffirmation(): String? {
        return dataStore.data.map { it[Keys.CACHED_AFFIRMATION] }.first()
    }

    suspend fun getAffirmationImageIndex(): Int {
        return dataStore.data.map { it[Keys.AFFIRMATION_IMAGE_INDEX] ?: 0 }.first()
    }

    suspend fun saveAffirmation(affirmation: String) {
        dataStore.edit {
            val nextIndex = Random.nextInt(0, 4)

            it[Keys.LAST_AFFIRMATION_TIMESTAMP] = System.currentTimeMillis()
            it[Keys.CACHED_AFFIRMATION] = affirmation
            it[Keys.AFFIRMATION_IMAGE_INDEX] = nextIndex
        }
    }


    suspend fun canFetchAdvice(): Boolean {
        val lastTimestamp = dataStore.data.map { it[Keys.LAST_ADVICE_TIMESTAMP] ?: 0L }.first()
        return has24HoursPassed(lastTimestamp)
    }

    suspend fun getCachedAdvice(): String? {
        return dataStore.data.map { it[Keys.CACHED_ADVICE] }.first()
    }

    suspend fun saveAdvice(advice: String) {
        dataStore.edit {
            it[Keys.LAST_ADVICE_TIMESTAMP] = System.currentTimeMillis()
            it[Keys.CACHED_ADVICE] = advice
        }
    }

    private fun has24HoursPassed(lastTimestamp: Long): Boolean {
        if (lastTimestamp == 0L) return true
        val diff = System.currentTimeMillis() - lastTimestamp
        return TimeUnit.MILLISECONDS.toHours(diff) >= 24
    }
}
