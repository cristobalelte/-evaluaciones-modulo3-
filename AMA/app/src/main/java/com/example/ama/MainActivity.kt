package com.example.ama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.Subcategory
import com.example.ama.ui.navigation.AppNavigation
import com.example.ama.ui.theme.AMATheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ama.ui.theme.ThemeOption
import com.example.ama.ui.theme.ThemePrefs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash Screen
        val splash = installSplashScreen()
        var keepSplash = true
        splash.setKeepOnScreenCondition { keepSplash }

        // Simula carga
        lifecycleScope.launch {
            delay(1500)
            keepSplash = false
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Usa el Context de Compose
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
                    onChangeTheme = { opt: ThemeOption ->
                        lifecycleScope.launch { prefs.setTheme(opt) }
                    },
                    themeOpt = themeOpt

                )
            }
        }
    }
}


