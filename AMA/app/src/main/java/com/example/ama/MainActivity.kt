package com.example.ama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ama.ui.components.PrimaryButton
import com.example.ama.ui.theme.AMATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AMATheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    PrimaryButton(
        text = "Hola",
        onClick = { }
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AMATheme {
        AppNavigation()
    }
}