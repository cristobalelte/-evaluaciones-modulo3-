package com.example.ama.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Componente que muestra la barra inferior (solo ejemplo, no es parte de la app)
@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 10.dp,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = "Inicio",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "AYUDA",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "Correo",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
//                Text(
//                    text = "Correo",
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Cuenta",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
//                Text(
//                    text = "Cuenta",
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
            }
        }
    }
}