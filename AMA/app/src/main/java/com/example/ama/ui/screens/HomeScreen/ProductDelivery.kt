@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ama.ui.screens.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DataEnvioScreen(

    cartCount: StateFlow<Int>,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onNext: () -> Unit
) {
    val count by cartCount.collectAsStateWithLifecycle(initialValue = 0)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier.height(28.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // (opcional) ajustes
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Configuración")
                    }
                    IconButton(onClick = onOpenCart) {
                        BadgedBox( badge = { if (count > 0) Badge { Text("$count") } }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomBar(onPublishClick = onOpenPublish) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Opciones de entrega",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            //  Opción: Envío a domicilio
            item {
                DeliveryCard(
                    titulo = "Llega mañana, 15 de septiembre",
                    precio = "$1.990",
                    boton = "ENVIAR A DOMICILIO",
                    onClick = onNext
                )
            }

            //  Opción: Punto de retiro
            item {
                DeliveryCard(
                    titulo = "Retira desde el 19 de septiembre",
                    precio = "Gratis",
                    boton = "PUNTO DE RETIRO",
                    onClick = onNext
                )
            }
        }
    }
}

@Composable
private fun DeliveryCard(
    titulo: String,
    precio: String,
    boton: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(precio, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                )
            ) { Text(boton) }
        }
    }
}
