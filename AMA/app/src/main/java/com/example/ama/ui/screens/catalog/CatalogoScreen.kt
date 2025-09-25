@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ama.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items as listItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.Subcategory
import com.example.ama.ui.components.label
import com.example.ama.ui.components.prettyLabel
import java.text.NumberFormat
import java.util.Locale

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
    subcategory: Subcategory? = null,          // 👈 NUEVO
    onBack: (() -> Unit)? = null,

    //  solo disponibles
    onlyAvailable: Boolean,
    onToggleOnlyAvailable: (Boolean) -> Unit,

    //  búsqueda
    query: String,
    onQueryChange: (String) -> Unit,

    //  filtros
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
        products, localQuery, localOnlyAvailable,
        initialType, subcategory, selectedRegions, selectedTypes
    ) {
        val q = localQuery.trim()
        products.filter { p ->
            (initialType == null || p.type == initialType) &&
                    (subcategory == null || p.subcategory == subcategory) &&
                    (!localOnlyAvailable || (p.isActive && p.stock > 0)) &&
                    (q.isBlank() || p.name.contains(q, true) || p.author.contains(q, true)) &&
                    (selectedRegions.isEmpty() || p.region in selectedRegions) &&
                    (selectedTypes.isEmpty() || p.type in selectedTypes)
        }
    }
    val listToShow = remember(filtered) { filtered.sortedByDescending { it.createdAt } }

// Forzar grilla cuando vengo desde subcategoría (mock de "Mantas")
    val forceGrid = subcategory != null
    val showGrid = forceGrid || localIsGrid

    val headerTitle = subcategory?.label()
        ?: initialType?.prettyLabel()
        ?: "Catálogo"

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header (buscador + título + filtros)
            Surface(tonalElevation = 0.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    // 1) Buscador tipo “pill”
                    Surface(shape = RoundedCornerShape(28.dp), shadowElevation = 4.dp) {
                        OutlinedTextField(
                            value = localQuery,
                            onValueChange = {
                                localQuery = it
                                onQueryChange(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            singleLine = true,
                            placeholder = { Text("¿Qué artesanía buscas?") },
                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                            trailingIcon = {
                                if (localQuery.isNotBlank()) {
                                    TextButton(onClick = {
                                        localQuery = ""
                                        onQueryChange("")
                                    }) { Text("Limpiar") }
                                }
                            },
                            shape = RoundedCornerShape(28.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }


                    // 2) Título de subcategoría
                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),

                    )

                    // 3) Filtros iguales y centrados
                    val chipW = 104.dp
                    val chipH = 32.dp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                    ) {
                        FilterPill("Ordenar\ny filtrar", chipW, chipH, twoLines = true) { /* TODO ordenar/filtrar */ }
                        FilterPill("Región",             chipW, chipH) { /* TODO región */ }
                        FilterPill("Precio",             chipW, chipH) { /* TODO precio */ }
                    }
                }
            }
            if (listToShow.isNotEmpty()) {
                Text(
                    text = "Agregados recientemente",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .fillMaxWidth()
                    ,
                    textAlign = TextAlign.Center,
                )
            }

            //Contenido
            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron productos")
                }
            } else {
                if (showGrid) {
                    LazyVerticalGrid(
                        columns = if (forceGrid) GridCells.Fixed(2) else GridCells.Adaptive(minSize = 160.dp),
                        state = gridState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        gridItems(listToShow, key = { it.id }) { p ->
                            ProductCardGrid(p, onAddToCart, onViewDetail) // 👈 NUEVA card
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        listItems(listToShow, key = { it.id }) { p ->
                            ProductItem(p, onAddToCart, onViewDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    width: Dp,
    height: Dp,
    twoLines: Boolean = false,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = label.uppercase(),
                maxLines = if (twoLines) 2 else 1,
                softWrap = twoLines,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                lineHeight = 12.sp
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        },
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .width(width)
            .height(height),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFF87513A),
            labelColor = Color.White,
            trailingIconContentColor = Color.White
        )
    )
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
@Composable
private fun ProductCardGrid(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es","CL")) }
    Card(
        onClick = { onViewDetail(product) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            // Precio como badge superior-izq
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = currency.format(product.price),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            // Corazón superior-der (placeholder)
            IconButton(
                onClick = { /* TODO: favorito */ },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorito")
            }
        }
        Column(Modifier.padding(8.dp)) {
            Text(
                product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                product.author,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}


