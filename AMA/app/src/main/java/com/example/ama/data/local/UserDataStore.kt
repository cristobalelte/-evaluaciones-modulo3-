package com.example.ama.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val USER_PREFS_NAME = "user_prefs"


val Context.userPrefsDataStore by preferencesDataStore(name = USER_PREFS_NAME)
