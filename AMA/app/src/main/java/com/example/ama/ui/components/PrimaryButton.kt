package com.example.ama.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit){

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            // en Color.kt y al tema en Theme.kt:
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        //El estilo del texto del btn lo definimos en Shape.kt y de la fuente en Theme.kt
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = text,
            // en Type.kt y a la fuente en Theme.kt:
            style = MaterialTheme.typography.labelSmall
        )
    }
}