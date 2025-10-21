package com.dominio.bloommind.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
data class UserProfile(
    val name: String,
    val email: String,
    val birthDate: String,
    val gender: String
)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile_prefs")
class ProfileRepository(context: Context) {
    private val dataStore = context.dataStore
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_BIRTH_DATE = stringPreferencesKey("user_birth_date")
        val USER_GENDER = stringPreferencesKey("user_gender")
    }
    val userProfileFlow: Flow<UserProfile?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val name = preferences[PreferencesKeys.USER_NAME]
            val email = preferences[PreferencesKeys.USER_EMAIL]
            val birthDate = preferences[PreferencesKeys.USER_BIRTH_DATE]
            val gender = preferences[PreferencesKeys.USER_GENDER]

            if (name != null && email != null && birthDate != null && gender != null) {
                UserProfile(name, email, birthDate, gender)
            } else {
                null
            }
        }
    suspend fun saveProfile(name: String, email: String,  birthDate: String, gender: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.USER_EMAIL] = email
            preferences[PreferencesKeys.USER_BIRTH_DATE] = birthDate
            preferences[PreferencesKeys.USER_GENDER] = gender
        }
    }
    suspend fun clearProfile() {
        dataStore.edit { it.clear() }
    }
}