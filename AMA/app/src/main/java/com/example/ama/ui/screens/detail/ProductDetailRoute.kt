package com.example.ama.ui.screens.detail


import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ama.ui.screens.catalog.CatalogViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailRoute(
    productId: String,
    onBack: () -> Unit,
    vm: CatalogViewModel
) {
    val vm: CatalogViewModel = viewModel()
    val product = remember(productId) { vm.getById(productId) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (product == null) { onBack(); return }

    ProductDetailScreen(
        product = product,
        onBack = onBack,
        onAddToCart = { p ->
            scope.launch {
                vm.addToCart(p)
                snackbar.showSnackbar("Agregado al carrito")
            }
        }
    )
}
