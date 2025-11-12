package com.example.ama.ui.screens.carrito

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle



@Composable
fun CartRoute(
    vm: CartViewModel,
    onBack: () -> Unit,
    onCheckoutSuccess: () -> Unit,
) {
    LaunchedEffect(Unit) {
        vm.refresh(owner = "usuario123")
    }
    // 1) Observa las filas de Room
    val rows = vm.rows.collectAsStateWithLifecycle(emptyList()).value

    // 2) Mapea a items para la UI
    val items = remember(rows) {
        rows.map { r ->
            CartViewModel.CartItem(            // Asegúrate que este data class exista en CartViewModel
                product = CartViewModel.UiProduct(
                    id = r.productId,
                    name = r.name ?: "",
                    price = r.price ?: 0.0,
                    imageUrl = r.imageUrl
                ),
                qty = r.qty
            )
        }
    }
    val total = remember(items) { vm.cartTotal() }

    CartScreen(
        items = items,                       // <-- List<CartViewModel.CartItem>
        total = total,
        onBack = onBack,
        onInc = { vm.inc(it) },
        onDec = { vm.dec(it) },
        onRemove = { vm.remove(it) },
        onClear = { vm.clear() },
        onCheckout = {
            vm.checkout(
                owner = "usuario123",
                onSuccess = onCheckoutSuccess,
                onFail = { /* TODO: snackbar/toast con error */ }
            )
        }
    )
}



