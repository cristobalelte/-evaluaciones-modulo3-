package com.example.ama.ui.screens.catalog

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.ama.ui.components.ProductType
import kotlinx.coroutines.launch

@Composable
fun CatalogRoute(
    vm: CatalogViewModel,
    navController: NavController,
    onViewDetail: (String) -> Unit,
    onOpenCart: () -> Unit,
    initialType: ProductType? = null,
    onBack: (() -> Unit)? = null,
) {
    val ctx = LocalContext.current

    // Carga catálogo + adjunta carrito una sola vez
    LaunchedEffect(Unit) {
        vm.loadFromDisk(ctx)
        vm.attachCart(context = ctx, owner = "usuario1") // usa aquí el usuario logueado real
    }

    // State de VM
    val products      by vm.products.collectAsStateWithLifecycle()
    val cartCount     by vm.cartCount.collectAsStateWithLifecycle()
    val onlyAvail     by vm.onlyAvailable.collectAsStateWithLifecycle()
    val query         by vm.query.collectAsStateWithLifecycle()
    val selectedRegs  by vm.regions.collectAsStateWithLifecycle()
    val selectedTypes by vm.types.collectAsStateWithLifecycle()

    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Layout
    var isGrid by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    CatalogScreen(
        products = products,
        cartCount = cartCount,
        snackbarHostState = snackbarHost,
        onAddToCart = { p ->
            scope.launch {
                vm.addToCart(p)
                snackbarHost.showSnackbar("Agregado")
            }
        },
        onViewDetail = { p -> onViewDetail(p.id) },
        onOpenCart = { navController.navigate("cart") },

        isGrid = isGrid,
        onToggleLayout = { isGrid = it },
        listState = listState,
        gridState = gridState,
        initialType = initialType,
        onBack = onBack,

        // “Solo disponibles”
        onlyAvailable = onlyAvail,
        onToggleOnlyAvailable = vm::setOnlyAvailable,

        // búsqueda
        query = query,
        onQueryChange = vm::setQuery,

        // filtros
        availableRegions = vm.availableRegions,
        selectedRegions = selectedRegs,
        onToggleRegion = vm::toggleRegion,

        availableTypes = vm.availableTypes,
        selectedTypes = selectedTypes,
        onToggleType = vm::toggleType,
    )
}
