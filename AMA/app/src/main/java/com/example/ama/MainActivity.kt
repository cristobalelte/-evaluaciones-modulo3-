package com.example.ama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ama.ui.navigation.AppNavigation
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.theme.AMATheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splash = installSplashScreen()
        var keepSplash = true
        splash.setKeepOnScreenCondition { keepSplash }

        lifecycleScope.launch {
            delay(1500)
            keepSplash = false
        }

        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        setContent {
            AMATheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                AppNavigation(
                    skipLogin = true
                )
            }
        }
    }
}

