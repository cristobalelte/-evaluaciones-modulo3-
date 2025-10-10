package com.example.ama.ui.screens.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import com.example.ama.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.screens.catalog.CatalogViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    items: List<CatalogViewModel.CartItem>,
    total: Double,
    onBack: () -> Unit,
    onInc: (String) -> Unit,
    onDec: (String) -> Unit,
    onRemove: (String) -> Unit,
    onClear: () -> Unit,
    onCheckout: () -> Unit
) {
    val money = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                },
                title = { Text("Carrito") },
                actions = {
                    if (items.isNotEmpty()) {
                        TextButton(onClick = onClear) { Text(stringResource(R.string.carro_vaciar)) }
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text(money.format(total), style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("datosEnvio") }, //
                    enabled = items.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor   = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text("CONTINUAR COMPRA") }

            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(stringResource(R.string.carro_vacio)) }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.product.id }) { item ->
                    CartRow(
                        item = item,
                        onInc = { onInc(item.product.id) },
                        onDec = { onDec(item.product.id) },
                        onRemove = { onRemove(item.product.id) }
                    )
                }
            }
        }
    }
}


@Composable
private fun CartRow(
    item: CatalogViewModel.CartItem,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    val money = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val cardBg = MaterialTheme.colorScheme.surfaceVariant

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(item.product.name, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                Text(money.format(item.product.price), color = MaterialTheme.colorScheme.primary)
                Text(
                    "Subtotal: " + money.format(item.product.price * item.qty),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))

                // Controles cantidad (+ 1 -)
                QuantityControl(
                    qty = item.qty,
                    onInc = onInc,
                    onDec = onDec
                )
            }

            // Botón Eliminar (rojo lleno, redondeado)
            Button(
                onClick = onRemove,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Eliminar")
            }
        }
    }
}
@Composable
private fun QuantityControl(
    qty: Int,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedIconButtonCircle(onClick = onInc) { Text("+") }
        Spacer(Modifier.width(12.dp))
        Text("$qty", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(12.dp))
        OutlinedIconButtonCircle(onClick = onDec) { Text("−") }
    }
}

@Composable
private fun OutlinedIconButtonCircle(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedIconButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor   = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.size(36.dp)
    ) { content() }
}

