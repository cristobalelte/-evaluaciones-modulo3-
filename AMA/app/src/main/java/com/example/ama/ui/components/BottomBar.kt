package com.example.ama.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    onHelpClick: () -> Unit = {},
    onPublishClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 10.dp,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = onHelpClick, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "Ayuda")
                    Text("AYUDA")
                }
            }
            IconButton(onClick = onPublishClick, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AddCircleOutline, contentDescription = "Publicar")
                    Text("Publicar")
                }
            }
            IconButton(onClick = onProfileClick, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    Text("Perfil")
                }
            }
        }
    }
}
