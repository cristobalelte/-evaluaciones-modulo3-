package com.example.ama.ui.screens.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import com.example.ama.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.ui.screens.catalog.CatalogViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
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
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                },
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
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text(money.format(total), style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onCheckout,
                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.pago_proceder))
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.carro_vacio))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
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

    Card(elevation = CardDefaults.cardElevation(2.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                Text(money.format(item.product.price), color = MaterialTheme.colorScheme.primary)
                Text("Subtotal: " + money.format(item.product.price * item.qty))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onDec) { Text("-") }
                Spacer(Modifier.width(8.dp))
                Text("${item.qty}")
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onInc) { Text("+") }
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

