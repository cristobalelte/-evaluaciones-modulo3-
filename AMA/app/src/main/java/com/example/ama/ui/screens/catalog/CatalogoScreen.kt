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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.R
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box


// ------------------------------------------------------------------------------------
// Modelo (incluye autor y disponibilidad para cumplir HU 31)
// ------------------------------------------------------------------------------------
enum class ProductType { TEXTIL, MADERA, CERAMICA, OTRO }

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val author: String,
    val isActive: Boolean = true,
    val stock: Int = 1,
    val region: String,             // 👈 Región (ej. "RM", "Biobío", etc.)
    val type: ProductType           // 👈 Tipo (TEXTIL/MADERA/CERAMICA/OTRO)
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
    cartCount: Int,
    snackbarHostState: SnackbarHostState,
    onAddToCart: (Product) -> Unit,
    onViewDetail: (Product) -> Unit,
    onOpenCart: () -> Unit,
    // layout
    isGrid: Boolean,
    onToggleLayout: (Boolean) -> Unit,
    listState: LazyListState,
    gridState: LazyGridState,
    // “Solo disponibles”
    onlyAvailable: Boolean,
    onToggleOnlyAvailable: (Boolean) -> Unit,

    // búsqueda
    query: String,
    onQueryChange: (String) -> Unit,

    // filtros
    availableRegions: List<String>,
    selectedRegions: Set<String>,
    onToggleRegion: (String) -> Unit,

    availableTypes: List<ProductType>,
    selectedTypes: Set<ProductType>,
    onToggleType: (ProductType) -> Unit
)
 {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.catalog_title)) },
                actions = {
                    // Cambiar lista <-> grilla
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }


                    // Switch "Solo disponibles"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Solo disp.", style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.width(6.dp))
                        Switch(checked = onlyAvailable, onCheckedChange = onToggleOnlyAvailable)
                    }
                    Spacer(Modifier.width(8.dp))


                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // ===== Encabezado: Buscador + Filtros =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // BUSCADOR
                OutlinedTextField(
                    value = query,                          // <- viene de props
                    onValueChange = onQueryChange,          // <- viene de props
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(id = R.string.Buscar_prod)) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Outlined.Close, contentDescription = stringResource(id = R.string.Limpiar_campo))
                            }
                        }
                    }
                )

                // FILTROS: Regiones
                MultiSelectDropdown(
                    label = stringResource(id = R.string.Region_prod),
                    items = availableRegions,
                    selected = selectedRegions,
                    onToggle = onToggleRegion,
                    onSelectAll = { selectAll ->
                        if (selectAll) {
                            availableRegions.forEach { if (it !in selectedRegions) onToggleRegion(it) }
                        } else {
                            selectedRegions.toList().forEach { onToggleRegion(it) }
                        }
                    },
                    itemLabel = { it }
                )

                Spacer(Modifier.height(8.dp))

                // FILTROS: Tipo de producto
                MultiSelectDropdown(
                    label = stringResource(id = R.string.Tipo_producto),
                    items = availableTypes,
                    selected = selectedTypes,
                    onToggle = onToggleType,
                    onSelectAll = { selectAll ->
                        if (selectAll) {
                            availableTypes.forEach { if (it !in selectedTypes) onToggleType(it) }
                        } else {
                            selectedTypes.toList().forEach { onToggleType(it) }
                        }
                    },
                    itemLabel = {
                        when (it) {
                            ProductType.TEXTIL   -> "Textil"
                            ProductType.MADERA   -> "Madera"
                            ProductType.CERAMICA -> "Cerámica"
                            ProductType.OTRO     -> "Otro"
                        }
                    }
                )

            // ===== Resultados =====
            if (products.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.No_encontrado_prod))
                }
            } else {
                if (isGrid) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        state = gridState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp),
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
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp),
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
    }
    }
 }

// Tarjeta "grande" para vista de lista

@Composable
fun ProductItem(
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
                    //Cambio de shape btn agregar al carrito:
                    shape = MaterialTheme.shapes.medium,
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



// Tarjeta "compacta"
@Composable
fun ProductCard(
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
@Composable
fun CatalogScreenPreview() {
    val sample = listOf(
        Product("1",stringResource(R.string.Bufanda_amano),15000.0,"","Juana Pérez", true,3,"RM", ProductType.TEXTIL),
        Product("2",stringResource(R.string.Ceramica_amano),25000.0,"","Cristóbal Elte", true,1,"Valparaíso", ProductType.CERAMICA)
    )
    val snackbar = remember { SnackbarHostState() }
    MaterialTheme {
        CatalogScreen(
            products = sample,
            cartCount = 2,
            snackbarHostState = snackbar,
            onAddToCart = {},
            onViewDetail = {},
            isGrid = true,
            onToggleLayout = {},
            listState = LazyListState(0,0),
            gridState = LazyGridState(),
            onlyAvailable = true,
            onToggleOnlyAvailable = {},
            query = "",
            onQueryChange = {},
            availableRegions = listOf("Araucanía","Biobío","RM","Valparaíso"),
            selectedRegions = emptySet(),
            onToggleRegion = {},
            availableTypes = ProductType.entries,
            selectedTypes = emptySet(),
            onToggleType = {},
            onOpenCart = {}
        )
    }
  }

     @OptIn(ExperimentalMaterial3Api::class)
     @Composable
     fun <T> MultiSelectDropdown(
         modifier: Modifier = Modifier,
         label: String,
         items: List<T>,
         selected: Set<T>,
         onToggle: (T) -> Unit,
         onSelectAll: (Boolean) -> Unit,        // true = seleccionar todos, false = limpiar
         itemLabel: (T) -> String = { it.toString() },

     ) {
         var expanded by remember { mutableStateOf(false) }

         ExposedDropdownMenuBox(
             expanded = expanded,
             onExpandedChange = { expanded = !expanded },
             modifier = modifier
         ) {
             OutlinedTextField(
                 readOnly = true,
                 value = when {
                     selected.isEmpty()            -> "Ninguno"
                     selected.size == items.size   -> "Todos"
                     else -> selected.joinToString(", ") { itemLabel(it) }
                 },
                 onValueChange = {},
                 label = { Text(label) },
                 trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                 modifier = Modifier
                     .menuAnchor()
                     .fillMaxWidth()
             )

             ExposedDropdownMenu(
                 expanded = expanded,
                 onDismissRequest = { expanded = false },
             ) {
                 // Seleccionar todos / Limpiar
                 DropdownMenuItem(
                     text = {
                         Text(if (selected.size == items.size) stringResource(R.string.Limpiar_seleccion) else stringResource(R.string.Sel_todos))
                     },
                     onClick = {
                         val selectAll = selected.size != items.size
                         onSelectAll(selectAll)
                     }
                 )

                 HorizontalDivider()

                 // Items con checkbox
                 items.forEach { item ->
                     val checked = item in selected
                     DropdownMenuItem(
                         text = {
                             Row(verticalAlignment = Alignment.CenterVertically) {
                                 Checkbox(checked = checked, onCheckedChange = null)
                                 Spacer(Modifier.width(8.dp))
                                 Text(itemLabel(item))
                             }
                         },
                         onClick = { onToggle(item) } // dejamos abierto para multiselección
                     )
                 }
             }
         }
     }




