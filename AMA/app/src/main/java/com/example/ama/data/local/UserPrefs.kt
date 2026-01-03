package com.example.ama.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefs(context: Context) {

    private val appContext = context.applicationContext
    private val dataStore = appContext.userPrefsDataStore

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_EMAIL = stringPreferencesKey("email")

        private val KEY_FIRSTNAME = stringPreferencesKey("firstName")
        private val KEY_LASTNAME = stringPreferencesKey("lastName")
        private val KEY_PHONE = stringPreferencesKey("phone")
        private val KEY_REGION = stringPreferencesKey("region")
        private val KEY_CITY = stringPreferencesKey("city")
        private val KEY_ADDRESS = stringPreferencesKey("address")
        private val KEY_ROLE = stringPreferencesKey("role")
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { it[KEY_TOKEN] }
    val emailFlow: Flow<String?> = dataStore.data.map { it[KEY_EMAIL] }
    val userIdFlow: Flow<String?> = dataStore.data.map { it[KEY_USER_ID] }
    val roleFlow: Flow<String?> = dataStore.data.map { it[KEY_ROLE] }

    val firstNameFlow: Flow<String?> = dataStore.data.map { it[KEY_FIRSTNAME] }
    val lastNameFlow: Flow<String?> = dataStore.data.map { it[KEY_LASTNAME] }
    val phoneFlow: Flow<String?> = dataStore.data.map { it[KEY_PHONE] }
    val regionFlow: Flow<String?> = dataStore.data.map { it[KEY_REGION] }
    val cityFlow: Flow<String?> = dataStore.data.map { it[KEY_CITY] }
    val addressFlow: Flow<String?> = dataStore.data.map { it[KEY_ADDRESS] }

    suspend fun saveAuth(token: String?, userId: String?, email: String?) {
        dataStore.edit { prefs ->
            if (token != null) prefs[KEY_TOKEN] = token
            if (userId != null) prefs[KEY_USER_ID] = userId
            if (email != null) prefs[KEY_EMAIL] = email
        }
    }

    suspend fun saveProfile(
        firstName: String,
        lastName: String,
        phone: String,
        region: String,
        city: String,
        address: String,
        role: String
    ) {
        dataStore.edit { prefs ->
            prefs[KEY_FIRSTNAME] = firstName
            prefs[KEY_LASTNAME] = lastName
            prefs[KEY_PHONE] = phone
            prefs[KEY_REGION] = region
            prefs[KEY_CITY] = city
            prefs[KEY_ADDRESS] = address
            prefs[KEY_ROLE] = role
        }
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
