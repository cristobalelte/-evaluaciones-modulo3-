package com.example.ama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ama.ui.navigation.AppNavigation
import com.example.ama.ui.theme.AMATheme
import com.example.ama.ui.theme.ThemeOption
import com.example.ama.ui.theme.ThemePrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash primero
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Mantener splash un ratito
        var keepSplash = true
        splash.setKeepOnScreenCondition { keepSplash }
        lifecycleScope.launch {
            delay(1500)
            keepSplash = false
        }

        enableEdgeToEdge()

        setContent {
            val ctx = LocalContext.current
            val prefs = remember(ctx) { ThemePrefs(ctx) }
            val themeOpt by prefs.themeFlow.collectAsState(initial = ThemeOption.SYSTEM)

            val dark = when (themeOpt) {
                ThemeOption.SYSTEM -> isSystemInDarkTheme()
                ThemeOption.DARK   -> true
                ThemeOption.LIGHT  -> false
            }

            AMATheme(darkTheme = dark) {
                AppNavigation(
                    skipLogin = false,
                    themeOpt = themeOpt,
                    onChangeTheme = { opt ->
                        lifecycleScope.launch { prefs.setTheme(opt) }
                    }
                )
            }
        }
    }
}


