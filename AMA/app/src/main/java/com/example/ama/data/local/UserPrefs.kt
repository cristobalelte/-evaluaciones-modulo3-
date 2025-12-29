package com.example.ama.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefs(private val context: Context) {

    companion object {
        // Auth
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_EMAIL = stringPreferencesKey("email")

        // Perfil local (extra)
        private val KEY_FIRSTNAME = stringPreferencesKey("firstName")
        private val KEY_LASTNAME = stringPreferencesKey("lastName")
        private val KEY_PHONE = stringPreferencesKey("phone")
        private val KEY_REGION = stringPreferencesKey("region")
        private val KEY_CITY = stringPreferencesKey("city")
        private val KEY_ADDRESS = stringPreferencesKey("address")
        private val KEY_ROLE = stringPreferencesKey("role")
    }

    //  Flows (por si quieres leerlos en UI)
    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }
    val emailFlow: Flow<String?> = context.dataStore.data.map { it[KEY_EMAIL] }
    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val roleFlow: Flow<String?> = context.dataStore.data.map { it[KEY_ROLE] }

    val firstNameFlow: Flow<String?> = context.dataStore.data.map { it[KEY_FIRSTNAME] }
    val lastNameFlow: Flow<String?> = context.dataStore.data.map { it[KEY_LASTNAME] }
    val phoneFlow: Flow<String?> = context.dataStore.data.map { it[KEY_PHONE] }
    val regionFlow: Flow<String?> = context.dataStore.data.map { it[KEY_REGION] }
    val cityFlow: Flow<String?> = context.dataStore.data.map { it[KEY_CITY] }
    val addressFlow: Flow<String?> = context.dataStore.data.map { it[KEY_ADDRESS] }

    //  Guardar auth
    suspend fun saveAuth(token: String?, userId: String?, email: String?) {
        context.dataStore.edit { prefs ->
            if (token != null) prefs[KEY_TOKEN] = token
            if (userId != null) prefs[KEY_USER_ID] = userId
            if (email != null) prefs[KEY_EMAIL] = email
        }
    }

    //  Guardar perfil local
    suspend fun saveProfile(
        firstName: String,
        lastName: String,
        phone: String,
        region: String,
        city: String,
        address: String,
        role: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FIRSTNAME] = firstName
            prefs[KEY_LASTNAME] = lastName
            prefs[KEY_PHONE] = phone
            prefs[KEY_REGION] = region
            prefs[KEY_CITY] = city
            prefs[KEY_ADDRESS] = address
            prefs[KEY_ROLE] = role
        }
    }

    //  Logout / limpiar
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
