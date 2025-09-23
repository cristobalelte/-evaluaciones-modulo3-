@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ama.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import java.text.NumberFormat
import java.util.Locale

// Evita conflictos: alias a "items" de lista y grilla
import androidx.compose.foundation.lazy.items as listItems
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
fun CatalogScreen(
    products: List<Product>,
    cartCount: Int,
    snackbarHostState: SnackbarHostState,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit,
    onOpenCart: () -> Unit,

    // --- layout / navegación ---
    isGrid: Boolean,
    onToggleLayout: (Boolean) -> Unit,
    listState: LazyListState,
    gridState: LazyGridState,
    initialType: ProductType? = null,
    onBack: (() -> Unit)? = null,

    // --- solo disponibles ---
    onlyAvailable: Boolean,
    onToggleOnlyAvailable: (Boolean) -> Unit,

    // --- búsqueda ---
    query: String,
    onQueryChange: (String) -> Unit,

    // --- filtros (se aplican aunque no dibujemos chips) ---
    availableRegions: List<String>,
    selectedRegions: Set<String>,
    onToggleRegion: (String) -> Unit,

    availableTypes: List<ProductType>,
    selectedTypes: Set<ProductType>,
    onToggleType: (ProductType) -> Unit,
) {
    // Estado local persistente
    var localQuery by rememberSaveable { mutableStateOf(query) }
    var localOnlyAvailable by rememberSaveable { mutableStateOf(onlyAvailable) }
    var localIsGrid by rememberSaveable { mutableStateOf(isGrid) }

    // Mantén sincronizado si el padre cambia
    LaunchedEffect(query) { localQuery = query }
    LaunchedEffect(onlyAvailable) { localOnlyAvailable = onlyAvailable }
    LaunchedEffect(isGrid) { localIsGrid = isGrid }

    // Filtrado memoizado
    val filtered: List<Product> = remember(
        products, localQuery, localOnlyAvailable, initialType, selectedRegions, selectedTypes
    ) {
        val q = localQuery.trim()
        products.filter { p ->
            (initialType == null || p.type == initialType) &&
                    (!localOnlyAvailable || (p.isActive && p.stock > 0)) &&
                    (q.isBlank() || p.name.contains(q, true) || p.author.contains(q, true)) &&
                    (selectedRegions.isEmpty() || p.region in selectedRegions) &&
                    (selectedTypes.isEmpty() || p.type in selectedTypes)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        localIsGrid = !localIsGrid
                        onToggleLayout(localIsGrid)
                    }) {
                        Icon(
                            imageVector = if (localIsGrid) Icons.Outlined.List else Icons.Outlined.GridView,
                            contentDescription = "Cambiar vista"
                        )
                    }
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Solo disp.")
                        Spacer(Modifier.width(6.dp))
                        Switch(
                            checked = localOnlyAvailable,
                            onCheckedChange = {
                                localOnlyAvailable = it
                                onToggleOnlyAvailable(it)
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BottomBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = localQuery,
                onValueChange = {
                    localQuery = it
                    onQueryChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Que artesania buscas?") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                trailingIcon = {
                    if (localQuery.isNotBlank()) {
                        TextButton(onClick = {
                            localQuery = ""
                            onQueryChange("")
                        }) { Text("Limpiar") }
                    }
                }
            )

            Spacer(Modifier.height(4.dp))

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron productos")
                }
            } else {
                if (localIsGrid) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        state = gridState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        gridItems(filtered, key = { it.id }) { p ->
                            ProductCard(p, onAddToCart, onViewDetail)
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        listItems(filtered, key = { it.id }) { p ->
                            ProductItem(p, onAddToCart, onViewDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onViewDetail(product) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                product.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                product.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(currency.format(product.price), color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onAddToCart(product) }
                ) { Text("Agregar al carrito") }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewDetail(product) }
                ) { Text("Ver detalle") }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    Card(onClick = { onViewDetail(product) }, elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                product.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                product.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Text(currency.format(product.price), color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Button(onClick = { onAddToCart(product) }, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar al carrito")
            }
        }
    }
}






