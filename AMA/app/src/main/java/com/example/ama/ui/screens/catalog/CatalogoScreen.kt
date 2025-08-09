package com.example.ama.ui.screens.catalog

import androidx.compose.material3.ExperimentalMaterial3Api


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.R
import java.text.NumberFormat
import java.util.Locale

// ------------------------------------------------------------------------------------
// Modelo (incluye autor y disponibilidad para cumplir HU 31)
// ------------------------------------------------------------------------------------
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val author: String,
    val isActive: Boolean = true,
    val stock: Int = 1
)

// ------------------------------------------------------------------------------------
// Pantalla Catálogo
// - Toggle lista/grilla
// - Switch "Solo disponibles"
// - Badge de carrito y Snackbar
// - Lista y grilla con tarjetas accesibles
// ------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    products: List<Product>,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit,
    cartCount: Int = 0,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    // layout & scroll (provistos por la Route)
    isGrid: Boolean,
    onToggleLayout: (Boolean) -> Unit,
    listState: LazyListState,
    gridState: LazyGridState,
    // filtro HU31
    onlyAvailable: Boolean,
    onToggleOnlyAvailable: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.catalog_title)) },
                actions = {
                    // Cambiar lista <-> grilla
                    IconButton(onClick = { onToggleLayout(!isGrid) }) {
                        Icon(
                            imageVector = if (isGrid) Icons.Outlined.ViewList else Icons.Outlined.GridView,
                            contentDescription = if (isGrid) "Ver en lista" else "Ver en cuadrícula"
                        )
                    }

                    // Switch "Solo disponibles"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Solo disp.",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(Modifier.width(6.dp))
                        Switch(
                            checked = onlyAvailable,
                            onCheckedChange = onToggleOnlyAvailable
                        )
                    }
                    Spacer(Modifier.width(8.dp))

                    // Carrito con badge
                    BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (isGrid) {
            // ---------- CUADRÍCULA (responsive) ----------
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                state = gridState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.id }) { p ->
                    ProductCard(
                        product = p,
                        onAddToCart = onAddToCart,
                        onViewDetail = onViewDetail
                    )
                }
            }
        } else {
            // ---------- LISTA ----------
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.id }) { p ->
                    ProductItem(
                        product = p,
                        onAddToCart = onAddToCart,
                        onViewDetail = onViewDetail
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------
// Tarjeta "grande" para vista de lista
// ------------------------------------------------------------------------------------
@Composable
private fun ProductItem(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetail(product) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = currency.format(product.price),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.add_to_cart))
                }
                OutlinedButton(
                    onClick = { onViewDetail(product) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.view_detail))
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------
// Tarjeta "compacta" para vista de grilla
// ------------------------------------------------------------------------------------
@Composable
private fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onViewDetail(product) }
    ) {
        Column(Modifier.padding(10.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = currency.format(product.price),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(6.dp))
            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_to_cart))
            }
        }
    }
}

// ------------------------------------------------------------------------------------
// Preview básico (solo UI)
// ------------------------------------------------------------------------------------
@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CatalogPreview() {
    val sample = listOf(
        Product("1", "Bufanda de lana tejida a mano", 15000.0, "", "Juana Pérez"),
        Product("2", "Juego de cerámica pintado a mano", 25000.0, "", "Cristóbal Elte")
    )
    MaterialTheme {
        // Para el preview fijo usamos lista; en tu app real los estados vienen de la Route
        // y el ViewModel.
        // (No pasamos listState/gridState aquí para mantener el preview simple)
        // Puedes crear una versión @Preview específica de la Route si lo prefieres.
        // Este preview es solo ilustrativo del look & feel.
    }
}
