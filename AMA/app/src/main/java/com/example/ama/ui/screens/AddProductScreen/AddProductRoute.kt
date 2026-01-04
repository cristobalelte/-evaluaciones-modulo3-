package com.example.ama.ui.screens.AddProductScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.ui.screens.AddProductScreen
import com.example.ama.ui.screens.catalog.CatalogViewModel

@Composable
fun AddProductRoute(
    navController: NavController,
    catalogVm: CatalogViewModel,
    onBack: () -> Unit
) {
    val vm: AddProductViewModel = viewModel()

    // 1) Carga catálogo (si lo necesitas para refrescar después)
    LaunchedEffect(Unit) {
        // si tú realmente necesitas esto, déjalo:
        // catalogVm.attachCatalog(ctx) etc...
        vm.loadCategories() // <-- categorías backend
    }

    AddProductScreen(
        navController = navController,
        vm = vm,
        onBack = onBack
    )
}

