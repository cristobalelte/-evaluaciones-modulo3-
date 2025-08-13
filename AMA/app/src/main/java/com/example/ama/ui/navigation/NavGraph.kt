package com.example.ama.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ama.ui.screens.carrito.CartRoute
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.detail.ProductDetailRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Única instancia de ViewModel compartida
    val vm: CatalogViewModel = viewModel()

    NavHost(navController, startDestination = "catalog") {

        //  Catálogo
        composable("catalog") {
            CatalogRoute(
                vm = vm,
                navController = navController,
                onViewDetail = { id -> navController.navigate("detail/$id") },
                onOpenCart = { navController.navigate("cart") }
            )
        }

        //  Detalle de producto
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            ProductDetailRoute(
                productId = productId,
                onBack = { navController.popBackStack() },
                vm = vm
            )
        }

        //  Carrito
        composable("cart") {
            CartRoute(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}



