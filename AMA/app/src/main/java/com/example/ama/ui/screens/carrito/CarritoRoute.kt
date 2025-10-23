package com.example.ama.ui.screens.carrito

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.ui.carrito.CartViewModel
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.Subcategory

import com.example.ama.ui.screens.catalog.CatalogViewModel

// ui/screens/carrito/CartRoute.kt
@Composable
fun CartRoute(
    navController: NavController,
    cartVm: CartViewModel = viewModel(
        factory = CartViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val rows by cartVm.rows.collectAsState(initial = emptyList())

    // Conviertes cada fila a tu CartItem para la UI
    val items = remember(rows) {
        rows.map { r ->
            val p = com.example.ama.ui.components.Product(
                id = r.productId,
                name = r.name,
                price = r.price,
                imageUrl = r.imageUrl ?: "",
                author = "", region = "",
                type = com.example.ama.ui.components.ProductType.OTRO,
                stock = 0,
                subcategory = com.example.ama.ui.components.Subcategory.OTROS,
                description = "",
                isActive = false
            )
            com.example.ama.ui.screens.catalog.CatalogViewModel.CartItem(product = p, qty = r.qty)
        }
    }

    val total = remember(items) { items.sumOf { it.product.price * it.qty } }

    CartScreen(
        navController = navController,
        items = items,
        total = total,
        onBack = { navController.popBackStack() },
        onInc = { id -> cartVm.inc(id) },
        onDec = { id -> cartVm.dec(id) },
        onRemove = { id -> cartVm.remove(id) },
        onClear = { cartVm.clear() },
        onCheckout = { navController.navigate("datosEnvio") }
    )
}

