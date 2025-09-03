package com.example.ama.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.ama.ui.theme.ThemeOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeOpt: ThemeOption,
    onChangeTheme: (ThemeOption) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .selectableGroup(), // accesibilidad
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Tema", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            listOf(
                ThemeOption.SYSTEM to "Usar tema del sistema",
                ThemeOption.LIGHT  to "Claro",
                ThemeOption.DARK   to "Oscuro",
            ).forEachIndexed { i, (opt, label) ->
                ListItem(
                    headlineContent = { Text(label) },
                    leadingContent = {
                        RadioButton(
                            selected = themeOpt == opt,
                            onClick = { onChangeTheme(opt) }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = themeOpt == opt,
                            onClick = { onChangeTheme(opt) },
                            role = Role.RadioButton
                        )
                )
                if (i < 2) Divider()
            }
        }
    }
}

