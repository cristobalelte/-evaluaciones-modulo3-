package com.example.ama.ui.screens.carrito

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.ama.ui.screens.catalog.CatalogViewModel

@Composable
fun CartRoute(
    vm: CatalogViewModel,
    onBack: () -> Unit
) {
    val items by vm.cartItems.collectAsStateWithLifecycle()
    val total = remember(items) { vm.cartTotal() }

    CartScreen(
        navController = rememberNavController(),
        items = items,
        total = total,
        onBack = onBack,
        onInc = { vm.incQty(it) },
        onDec = { vm.decQty(it) },
        onRemove = { vm.removeFromCart(it) },
        onClear = { vm.clearCart() },
        onCheckout = { /* TODO */ }

    )
}

