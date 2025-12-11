package com.example.ama.ui.screens.detail



import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.ama.ui.components.Product
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.catalog.CatalogRoute
import kotlinx.coroutines.launch

//NO SE ESTA USANDO ESTE COMPOSE
@Composable
fun ProductDetailRoute(
    navController: NavController,
    productId: String,
    onBack: () -> Unit,
    vm: CatalogViewModel,
    onAddToCart: (Product) -> Unit,
    onOpenCart: () -> Unit,
) {

    val cartCount by vm.cartCount.collectAsState(initial = 0)
    val product = remember(productId) { vm.getById(productId) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (product == null) {
        onBack()
        return
    }

    ProductDetailScreen(
        navController = navController,
        product = product,
        onOpenPublish = { /* ... */ },
        onBack = onBack,
        onAddToCart = { p ->
            scope.launch {
                vm.addToCart(p)
                snackbar.showSnackbar("Agregado al carrito")
            }
        },
        cartCount = cartCount,
        onOpenCart = onOpenCart
    )
}

