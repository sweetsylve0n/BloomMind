package com.dominio.bloommind.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

private val Context.requestQuotaDataStore: DataStore<Preferences> by preferencesDataStore(name = "request_quotas")

class LocalContentDataSource(context: Context) {

    private val dataStore = context.requestQuotaDataStore
    private object Keys {
        val LAST_AFFIRMATION_DATE = stringPreferencesKey("last_affirmation_date")
        val CACHED_AFFIRMATION = stringPreferencesKey("cached_affirmation")
        val AFFIRMATION_IMAGE_INDEX = intPreferencesKey("affirmation_image_index")
        val LAST_ADVICE_DATE = stringPreferencesKey("last_advice_date")
        val CACHED_ADVICE = stringPreferencesKey("cached_advice")
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun canFetchAffirmation(): Boolean {
        val lastDate = dataStore.data.map { it[Keys.LAST_AFFIRMATION_DATE] ?: "" }.first()
        return lastDate != getCurrentDateString()
    }

    suspend fun getCachedAffirmation(): String? {
        return dataStore.data.map { it[Keys.CACHED_AFFIRMATION] }.first()
    }

    suspend fun getAffirmationImageIndex(): Int {
        return dataStore.data.map { it[Keys.AFFIRMATION_IMAGE_INDEX] ?: 0 }.first()
    }

    suspend fun saveAffirmation(affirmation: String) {
        dataStore.edit {
            val nextIndex = Random.nextInt(0, 32)
            it[Keys.LAST_AFFIRMATION_DATE] = getCurrentDateString()
            it[Keys.CACHED_AFFIRMATION] = affirmation
            it[Keys.AFFIRMATION_IMAGE_INDEX] = nextIndex
        }
    }

    suspend fun canFetchAdvice(): Boolean {
        val lastDate = dataStore.data.map { it[Keys.LAST_ADVICE_DATE] ?: "" }.first()
        return lastDate != getCurrentDateString()
    }

    suspend fun getCachedAdvice(): String? {
        return dataStore.data.map { it[Keys.CACHED_ADVICE] }.first()
    }

    suspend fun saveAdvice(advice: String) {
        dataStore.edit {
            it[Keys.LAST_ADVICE_DATE] = getCurrentDateString()
            it[Keys.CACHED_ADVICE] = advice
        }
    }
}
