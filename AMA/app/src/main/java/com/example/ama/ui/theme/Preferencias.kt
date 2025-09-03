package com.example.ama.ui.theme


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class ThemeOption { SYSTEM, LIGHT, DARK }

val Context.dataStore by preferencesDataStore("user_prefs")

class ThemePrefs(private val context: Context) {

    private object Keys { val THEME = stringPreferencesKey("theme_option") }

    val themeFlow = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            when (prefs[Keys.THEME]) {
                ThemeOption.LIGHT.name -> ThemeOption.LIGHT
                ThemeOption.DARK.name  -> ThemeOption.DARK
                else                   -> ThemeOption.SYSTEM
            }
        }

    suspend fun setTheme(option: ThemeOption) {
        context.dataStore.edit { it[Keys.THEME] = option.name }
    }
}

