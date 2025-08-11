package com.example.ama.ui.screens.detail



import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.catalog.Product
import kotlinx.coroutines.launch

@Composable
fun ProductDetailRoute(
    productId: String,
    onBack: () -> Unit,
    vm: CatalogViewModel
) {
    // ❌ NO crear otro viewModel() aquí.
    // ✅ Tomar el producto del vm que llega por parámetro
    val product = remember(productId) { vm.getById(productId) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (product == null) {
        onBack()
        return
    }

    ProductDetailScreen(
        product = product as Product,
        onBack = onBack,
        onAddToCart = {
            scope.launch {
                vm.addToCart(product)
                snackbar.showSnackbar("Agregado al carrito")
            }
        },
        // si tu ProductDetailScreen admite snackbarHostState, pásalo:
        // snackbarHostState = snackbar
    )
}

