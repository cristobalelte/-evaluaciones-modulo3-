package com.example.ama.ui.screens.perfil

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.screens.catalog.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRoute(
    vm: CatalogViewModel,
    navController: NavController,
    onViewDetail: (String) -> Unit,
    onOpenCart: () -> Unit,
    initialType: ProductType? = null,
    onBack: (() -> Unit)? = null

){
    /*
    val ctx = LocalContext.current

    // Carga catálogo + adjunta carrito una sola vez
    LaunchedEffect(Unit) {
        vm.loadFromDisk(ctx)
        vm.attachCart(ctx)
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

    PerfilScreen(
        navController = NavController(ctx),
        cartCount = cartCount,
        onOpenCart = onOpenCart,
        onOpenPublish = { navController.navigate("publish") },
        onOpenSettings = { navController.navigate("settings") }
    )
    */

}
