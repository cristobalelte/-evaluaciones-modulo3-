package com.example.ama.ui.screens.catalog

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController


@Composable
fun CatalogRoute(
    vm: CatalogViewModel,
    navController: NavController,
    onViewDetail: (String) -> Unit,
    onOpenCart: () -> Unit
) {
    val products       by vm.products.collectAsStateWithLifecycle()
    val cartCount      by vm.cartCount.collectAsStateWithLifecycle()
    val onlyAvail      by vm.onlyAvailable.collectAsStateWithLifecycle()
    val query          by vm.query.collectAsStateWithLifecycle()
    val selectedRegs   by vm.regions.collectAsStateWithLifecycle()
    val selectedTypes  by vm.types.collectAsStateWithLifecycle()

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // layout y estados de scroll
    var isGrid by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) { vm.refresh() }

    CatalogScreen(
        // datos y acciones básicas
        products = products,
        cartCount = cartCount,
        snackbarHostState = snackbar,
        onAddToCart = { p -> scope.launch { vm.addToCart(p); snackbar.showSnackbar("Agregado") } },
        onViewDetail = { p -> onViewDetail(p.id) },
        onOpenCart = { navController.navigate("cart") },
        // layout
        isGrid = isGrid,
        onToggleLayout = { isGrid = it },
        listState = listState,
        gridState = gridState,

        // switch “Solo disponibles”
        onlyAvailable = onlyAvail,
        onToggleOnlyAvailable = { vm.setOnlyAvailable(it) },

        // 🔎 búsqueda + filtros
        query = query,
        onQueryChange = { vm.setQuery(it) },

        availableRegions = vm.availableRegions,
        selectedRegions = selectedRegs,
        onToggleRegion = { r -> vm.toggleRegion(r) },

        availableTypes = vm.availableTypes,      // << te faltaba
        selectedTypes = selectedTypes,           // << te faltaba
        onToggleType = { t -> vm.toggleType(t) } // << te faltaba
    )
}
