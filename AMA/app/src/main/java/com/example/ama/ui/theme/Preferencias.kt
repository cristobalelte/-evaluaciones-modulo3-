package com.example.ama.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ama.data.local.userPrefsDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class ThemeOption { SYSTEM, LIGHT, DARK }

class ThemePrefs(context: Context) {

    private val appContext = context.applicationContext
    private val dataStore = appContext.userPrefsDataStore

    private object Keys { val THEME = stringPreferencesKey("theme_option") }

    val themeFlow = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            when (prefs[Keys.THEME]) {
                ThemeOption.LIGHT.name -> ThemeOption.LIGHT
                ThemeOption.DARK.name  -> ThemeOption.DARK
                else                   -> ThemeOption.SYSTEM
            }
        }

    suspend fun setTheme(option: ThemeOption) {
        dataStore.edit { it[Keys.THEME] = option.name }
    }
}
