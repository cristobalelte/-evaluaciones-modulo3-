package com.example.ama.ui.screens.AddProductScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.ui.screens.catalog.CatalogViewModel

@Composable
fun AddProductRoute(
    navController: NavController,
    catalogVm: CatalogViewModel,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        catalogVm.attachCatalog(ctx)
        catalogVm.attachCart(ctx)
        catalogVm.loadFromDisk(ctx)
    }

    val addVm: AddProductViewModel = viewModel()

    AddProductScreen(
        navController = navController,
        cartCount = cartCount,
        onOpenCart = onOpenCart,
        vm = addVm,
        onBack = onBack
    )
}

