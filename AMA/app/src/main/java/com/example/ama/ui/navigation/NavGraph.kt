package com.example.ama.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.detail.ProductDetailRoute

// ...

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 👇 única instancia compartida entre pantallas
    val vm: CatalogViewModel = viewModel()

    NavHost(navController, startDestination = "catalog") {
        composable("catalog") {
            CatalogRoute(
                vm = vm,
                onViewDetail = { id -> navController.navigate("detail/$id") }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id"){ type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            ProductDetailRoute(
                vm = vm,                          // 👈 misma instancia
                productId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


